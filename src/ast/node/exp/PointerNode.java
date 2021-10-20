package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class PointerNode implements Node {

  private Integer val;
  private TypeNode type;
  
  public PointerNode (Integer n) {
    val=n;
  }
  
  public String toPrint(String s) {
    return s+"Pointer:" + Integer.toString(val) +"\n";  
  }
  
  public TypeNode typeCheck() {
    return new PointerTypeNode(type);
  } 
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

  @Override
  public ArrayList<SemanticError> checkEffects(Environment env) {
    return null;
  }

  public String codeGeneration(Label labelManager) throws SimplanPlusException {

      return "push "+val+"\n";
  }

}  