package ast.node.statements;

import java.util.ArrayList;

import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.ExpNode;
import ast.node.exp.IdExpNode;
import ast.node.types.*;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class PrintNode extends MetaNode {

  private Node val;
  
  public PrintNode (Node v) {
    val=v;
  }
  
  public String toPrint(String s) {
    return s+"Print\n" + val.toPrint(s+"  ") ;
  }
  
  public TypeNode typeCheck() {
    TypeNode valType = val.typeCheck();
    if(valType instanceof PointerTypeNode){
      System.err.println("Trying to print a pointer "+((IdExpNode)val).getID());
      System.exit(0);
    }
      //throw new SimplanPlusException("Print of a pointer");
    return valType;
  }  
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

 	  return val.checkSemantics(env);
 	}
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
      ArrayList<EffectError> errors = new ArrayList<>();
      errors.addAll(val.checkEffects(env));
      return errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        StringBuilder cgen = new StringBuilder();
        cgen.append(val.codeGeneration(labelManager)).append("\n");
        cgen.append("print $a0\n");
		return cgen.toString();
  }
    
}  