package ast.node.statements;

import ast.STentry;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.dec.VarNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;

import java.util.*;
import java.util.stream.Collectors;

import ast.Label;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class BlockNode implements Node {

  private final ArrayList<Node> declarations;
  private final ArrayList<Node> statements;
  private final Boolean isMain;
  private Boolean isFunction;
  private int current_nl;

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
          env.nestingLevel++;
          env.symTable.add(hm);
      }
      current_nl = env.nestingLevel;
      /*System.out.println("CURRENT NESTING LEVEL IN BLOCK AND IS FUNCTION");
      System.out.println(current_nl);
      System.out.println(isFunction);*/


      //declare resulting list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      if(isFunction)
          env.offset = -2;
      else
          env.offset = -1;
      //System.out.println("OFFSET PRIMA DEC "+env.offset);

      //check semantics in the dec list
      if(declarations.size() > 0){
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : declarations)
    		  res.addAll(n.checkSemantics(env));
      }

      //System.out.println("OFFSET DOPO DEC &isfun: "+isFunction+" "+env.offset);

      if(statements.size() > 0){
    	  //if there are children then check semantics for every child and save the results
    	  for(Node n : statements)
    		  res.addAll(n.checkSemantics(env));
      }

      //check semantics in the exp body      
      //clean the scope, we are leaving a let scope
      if(!isFunction){
          env.symTable.remove(env.nestingLevel--);
      }

      //return the result
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
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  RetEffType tmp = new RetEffType(RetEffType.RetT.ABS);
	  for (Node s:statements) {
			  tmp= RetEffType.max(tmp, s.retTypeCheck(funNode));
	  }
	  return tmp;
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return new ArrayList<>();
    }

//  public ArrayList<SemanticError> delTypeCheck(DelEnv env, int n){
//	  ArrayList<SemanticError> err = new ArrayList<SemanticError>();
//	  
//	  for (Node i: declarations) {
//		  err.addAll(i.delTypeCheck(env, n));
//	  }
//	  
//	  for (Node i: statements) {
//		  if (!(i instanceof BlockNode))
//			  err.addAll(i.delTypeCheck(env, n+1));
//		  else
//			  err.addAll(i.delTypeCheck(env, n));
//	  }
//
//	  return err;
//  }
  
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

      if (funDec.size() > 0)
         cgen.append("//CREO FUNZIONI\n");
      for (Node fun:funDec){
          cgen.append(fun.codeGeneration(labelManager)).append("\n");
      }
      if (funDec.size() > 0)
          cgen.append("//FINE FUNZIONI\n");
	  return  cgen.toString();

  } 
  
  
    
}  