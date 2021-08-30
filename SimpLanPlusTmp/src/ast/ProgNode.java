package ast;

import java.util.ArrayList;

import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class ProgNode implements Node {

  private Node exp;
  
  public ProgNode (Node e) {
    exp=e;
  }
  
  public String toPrint(String s) {
    
    return "Prog\n" + exp.toPrint("  ") ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		return exp.checkSemantics(env);
	}
  
  public TypeNode typeCheck() {
    return exp.typeCheck();
  }  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
		return exp.codeGeneration()+"halt\n";
  }  
  
}  