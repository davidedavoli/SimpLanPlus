package ast.node.statements;

import java.util.ArrayList;

import GraphEffects.EffectsManager;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.CallExpNode;
import ast.node.exp.ExpNode;
import ast.node.exp.LhsExpNode;
import ast.node.exp.single_exp.NewNode;
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
  
  public String toPrint(String s) throws SimplanPlusException {
    return s+"Print\n" + val.toPrint(s+"  ") ;
  }
  
  public TypeNode typeCheck() throws SimplanPlusException {
    TypeNode valType = val.typeCheck();
    if(valType instanceof PointerTypeNode)
      throw new SimplanPlusException("Print of a pointer");
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
  public void checkGraphEffects (EffectsManager m) {
    ((ExpNode)val).checkGraphEffects(m);
  }

}  