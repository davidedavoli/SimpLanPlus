package ast.node.statements;

import java.util.ArrayList;

import ast.node.MetaNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
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
    if (!(valType instanceof BoolTypeNode || valType instanceof IntTypeNode))
      throw new SimplanPlusException("Print of a pointer");
    return valType;
  }  
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

 	  return val.checkSemantics(env);
 	}
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
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