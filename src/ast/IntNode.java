package ast;

import java.util.ArrayList;

import types.IntTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class IntNode implements Node {

  private Integer val;
  
  public IntNode (Integer n) {
    val=n;
  }
  
  public String toPrint(String s) {
    return s+"Int:" + Integer.toString(val) +"\n";  
  }
  
  public TypeNode typeCheck() {
    return new IntTypeNode();
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