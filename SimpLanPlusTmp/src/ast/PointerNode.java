package ast;

import java.util.ArrayList;

import types.PointerTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
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
  
  public String codeGeneration() {
	return "push "+val+"\n";
  }

}  