package ast.node.types;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class IntTypeNode implements TypeNode {
  
  public IntTypeNode () {
  }
  
  public String toPrint(String s) {
	return s+"IntType\n";  
  }
  public TypeNode dereference() throws SimplanPlusException {
      throw new SimplanPlusException("Attempt to dereference an int");
  }
  //non utilizzato
  public TypeNode typeCheck() {
    return null;
  }

  //non utilizzato
  public String codeGeneration(Label labelManager) {
		return "";
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return null;
    }

    @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

	  return new ArrayList<SemanticError>();
	}
  
}  