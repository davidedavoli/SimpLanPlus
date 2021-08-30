package ast;

import java.util.ArrayList;

import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class PrintNode implements Node {

  private Node val;
  
  public PrintNode (Node v) {
    val=v;
  }
  
  public String toPrint(String s) {
    return s+"Print\n" + val.toPrint(s+"  ") ;
  }
  
  public TypeNode typeCheck() {
    return val.typeCheck();
  }  
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return val.checkSemantics(env);
 	}
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
		return val.codeGeneration()+"print\n";
  }
    
}  