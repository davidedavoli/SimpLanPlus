package ast.node.statements;

import ast.STentry;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.dec.VarNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;

import java.util.*;
import java.util.stream.Collectors;

import ast.Label;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class BlockNode extends MetaNode {

  private final ArrayList<Node> declarations;
  private final ArrayList<Node> statements;
  private final Boolean isMain;
  private Boolean isFunction;
  private int current_nl;
  private String missingReturnCode = "";


    public int getCurrent_nl() {
        return current_nl;
    }

    public BlockNode(ArrayList<Node> d, ArrayList<Node> s, Boolean isMainBlock) {
        declarations=d;
        statements=s;
        isMain = isMainBlock;
        isFunction = false;
    }
    public void setIsFunction(Boolean isFunctionBlock){
      isFunction = isFunctionBlock;
    }

    public String toPrint(String s) throws SimplanPlusException {
	String declstr="";
	String statstr="";
    for (Node dec:declarations)
      declstr += dec.toPrint(s+"  ");
    for (Node stat:statements)
      statstr += stat.toPrint(s+"  ");
	return s+"Block\n" + declstr + statstr ; 
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
      HashMap<String, STentry> hm = new HashMap<String,STentry> ();

      if (!isFunction) {
          env.createVoidScope();
      }
      current_nl = env.getNestingLevel();

      //declare resulting list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      if(isFunction)
          env.functionOffset();
      else
          env.blockOffset();

      //check semantics in the dec list
      if(declarations.size() > 0){
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : declarations)
    		  res.addAll(n.checkSemantics(env));
      }


      if(statements.size() > 0) {
          //if there are children then check semantics for every child and save the results
          for (Node n : statements)
              res.addAll(n.checkSemantics(env));
          res.addAll(this.checkCodeAfterRet());
      }

      //check semantics in the exp body      
      //clean the scope, we are leaving a let scope
      if(!isFunction){
          env.popBlockScope();
      }

      //return the result
      return res;
	}

    private List<SemanticError> checkCodeAfterRet() {
        ArrayList<SemanticError> res = new ArrayList<>();
        boolean ret_passed= false;
        for(Node n : statements) {
            if (ret_passed){
                res.add(new SemanticError("Code after return statement"));
                break;
            }
            if (n instanceof RetNode)
                ret_passed = true;
        }
        return res;
    }

    public TypeNode typeCheck () throws SimplanPlusException {
	  //ne siamo sicuri?
	TypeNode last =null;
    for (Node dec:declarations)
      last = dec.typeCheck();
    for (Node stat:statements)
      last = stat.typeCheck();
    return last;
 }
  
  public ArrayList<TypeNode> getReturnList() throws SimplanPlusException {
      ArrayList<TypeNode> res = new ArrayList<TypeNode>();
      for (Node s: statements) {
    	  if (s instanceof BlockNode)
    		  res.addAll(((BlockNode) s).getReturnList());//Warning  Casting
    	  else if (s instanceof RetNode) {
    		  res.add(s.typeCheck());
    		  break;
    	  }
      }
      return res;
  }
  
  public HasReturn retTypeCheck() {
	  HasReturn tmp = new HasReturn(HasReturn.hasReturnType.ABS);
	  for (Node s:statements) {
			  tmp= HasReturn.max(tmp, s.retTypeCheck());
	  }
	  return tmp;
  }


    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();

        if (!isFunction) {
            env.createVoidScope();
        }

        for (Node dec : declarations) {
            errors.addAll(dec.checkEffects(env));
        }

        for(Node stm: statements) {
            errors.addAll(stm.checkEffects(env));
        }

        if (!isFunction) {
            env.popBlockScope();
        }
        return errors;
    }

  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();


      /**
       * Activation link
       */
        if (!isFunction){
            cgen.append("push 0\n");

            if (!isMain) {
              cgen.append("push $fp //loadind new block\n");
            }

            cgen.append("mv $sp $fp //Load new $fp\n");

        }

      Collection<Node> varDec = declarations.stream().filter(dec -> dec instanceof VarNode).collect(Collectors.toList());
      Collection<Node> funDec = declarations.stream().filter(fun -> fun instanceof FunNode).collect(Collectors.toList());

      for (Node dec:varDec)
            cgen.append(dec.codeGeneration(labelManager)).append("\n");


	  for (Node stat:statements)
          cgen.append(stat.codeGeneration(labelManager)).append("\n");

      if(!isFunction){
           if(isMain){
               cgen.append("halt\n");
           }
           else{
                cgen.append("subi $sp $fp 1 //Restore stackpointer as before block creation in blockNode\n");
                cgen.append("lw $fp 0($fp) //Load old $fp pushed \n");
            }
      }
      else{
          cgen.append(this.missingReturnCode);
          this.missingReturnCode = "";
      }

      if (funDec.size() > 0)
         cgen.append("//CREO FUNZIONI\n");
      for (Node fun:funDec){
          cgen.append(fun.codeGeneration(labelManager)).append("\n");
      }
      if (funDec.size() > 0)
          cgen.append("//FINE FUNZIONI\n");
	  return  cgen.toString();

  }

    public void addMissingReturnFunctionCode(String missingReturnCode) {
        this.missingReturnCode = missingReturnCode;
    }
}