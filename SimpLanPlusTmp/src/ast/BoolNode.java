package ast;

import java.util.ArrayList;

import types.BoolTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class BoolNode implements Node {

  private boolean val;
  
  public BoolNode (boolean n) {
    val=n;
  }
  
  public String toPrint(String s) {
    if (val) return s+"Bool:true\n";
    else return s+"Bool:false\n";  
  }
  
  public TypeNode typeCheck() {
    return new BoolTypeNode();
  }    
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  public String codeGeneration() {
		return "push "+(val?1:0)+"\n";
	  }
         
}  