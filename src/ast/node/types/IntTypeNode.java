package ast.node.types;

import java.util.ArrayList;

import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class IntTypeNode implements TypeNode {
  
  public IntTypeNode () {
  }
  
  public String toPrint(String s) {
	return s+"IntType\n";  
  }
  public TypeNode dereference() {
      System.out.println("Attempt to dereference an int");
      System.exit(0);
	  return null;
  }
  //non utilizzato
  public TypeNode typeCheck() {
    return null;
  }

  //non utilizzato
  public String codeGeneration(Label labelManager) {
		return "";
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

    @Override
    public int getDereferenceLevel() {
        return 1;
    }
  
}  