package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.PointerTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;

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
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {

      return "push "+val+"\n";
  }

}  