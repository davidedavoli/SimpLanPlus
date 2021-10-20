package ast.node.types;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class VoidTypeNode implements TypeNode {
  
  public VoidTypeNode () {
  }
  
  public String toPrint(String s) {
	return s+"VoidType\n";  
  }
  
  public TypeNode dereference() {
      System.out.println("Attempt to dereference a void");
      System.exit(0);
      return null;
  }
    
  //non utilizzato
  public TypeNode typeCheck() {
    return null;
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
  
  //non utilizzato
  public String codeGeneration(Label labelManager){
		return "";
  }

    
}  