package ast.node.types;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

public class BoolTypeNode implements TypeNode {
  
  public BoolTypeNode () {
  }
  
  public String toPrint(String s) {
	return s+"BoolType\n";  
  }
  
  public TypeNode dereference() throws SimplanPlusException {
      throw new SimplanPlusException("Attempt to dereference a bool");
  }
    
  //non utilizzato
  public TypeNode typeCheck() {
    return null;
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  //non utilizzato
  public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  