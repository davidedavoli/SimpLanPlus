package ast.node.types;

import java.util.ArrayList;

import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class PointerTypeNode implements TypeNode {
  private TypeNode pointedType;
  
  public PointerTypeNode (TypeNode t) {
	  pointedType =t;
  }
  
  public String toPrint(String s) {
	return s+"Pointer type\n"+ pointedType.toPrint(s+"   ");
  }
    
  //non utilizzato
  public TypeNode typeCheck() {
    return null; //non bisognerebbe chiamare typecheck su questo nodo
  }

    @Override
    public int getDereferenceLevel() {
        return 1 + this.pointedType.getDereferenceLevel();
    }

    @Override
    public TypeNode getPointedType() {
        return this.pointedType;
    }


  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return null;
    }

    //TODO Generare il codice
  public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  