package ast.node.statements;

import ast.Label;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.dec.VarNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public String toPrint(String s) {
	StringBuilder declarationString= new StringBuilder();
	StringBuilder statementString= new StringBuilder();
    for (Node dec:declarations)
      declarationString.append(dec.toPrint(s + "  "));
    for (Node stat:statements)
        statementString.append(stat.toPrint(s + "  "));
	return s+"Block\n" + declarationString + statementString ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
      if (!isFunction) {
          env.createVoidScope();
      }
      current_nl = env.getNestingLevel();

      //declare resulting list
      ArrayList<SemanticError> res = new ArrayList<>();
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

    public TypeNode typeCheck() {
        TypeNode last =null;
        for (Node dec:declarations)
          last = dec.typeCheck();
        for (Node stat:statements)
          last = stat.typeCheck();
        return last;
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

  
  public String codeGeneration(Label labelManager) {
      StringBuilder codeGenerated = new StringBuilder();


      /**
       * Activation link
       */
        if (!isFunction){
            codeGenerated.append("push 0\n");

            if (!isMain) {
              codeGenerated.append("push $fp //loading new block\n");
            }

            codeGenerated.append("mv $sp $fp //Load new $fp\n");

        }

      Collection<Node> varDec = declarations.stream().filter(dec -> dec instanceof VarNode).collect(Collectors.toList());
      Collection<Node> funDec = declarations.stream().filter(fun -> fun instanceof FunNode).collect(Collectors.toList());

      for (Node dec:varDec)
            codeGenerated.append(dec.codeGeneration(labelManager)).append("\n");


	  for (Node stat:statements)
          codeGenerated.append(stat.codeGeneration(labelManager)).append("\n");

      if(!isFunction){
           if(isMain){
               codeGenerated.append("halt\n");
           }
           else{
                codeGenerated.append("subi $sp $fp 1 //Restore stack pointer as before block creation in blockNode\n");
                codeGenerated.append("lw $fp 0($fp) //Load old $fp pushed \n");
            }
      }
      else{
          codeGenerated.append(this.missingReturnCode);
          this.missingReturnCode = "";
      }

      if (funDec.size() > 0)
         codeGenerated.append("//Creating function:\n");
      for (Node fun:funDec){
          codeGenerated.append(fun.codeGeneration(labelManager)).append("\n");
      }
      if (funDec.size() > 0)
          codeGenerated.append("//Ending function.\n");
	  return  codeGenerated.toString();

  }

    public void addMissingReturnFunctionCode(String missingReturnCode) {
        this.missingReturnCode = missingReturnCode;
    }
}