package ast.node.types;

import java.util.ArrayList;

import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

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

    @Override
    public int getDereferenceLevel() {
        return 1;
    }

    public RetEffType retTypeCheck() {
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
  public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  