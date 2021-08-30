package types;

import java.util.ArrayList;

import util.Environment;
import util.SemanticError;

public class PointerTypeNode implements TypeNode {
  private TypeNode type;
  
  public PointerTypeNode (TypeNode t) {
	  type=t;
  }
  
  public String toPrint(String s) {
	return s+"Pointer type\n"+type.toPrint(s+"   ");  
  }
    
  //non utilizzato
  public TypeNode typeCheck() {
    return null; //non bisognerebbe chiamare typecheck su questo nodo
  }
  
  public TypeNode dereference() {
	    return type;
	  }
  
  public int getDerefLevel(){
	  if (type instanceof PointerTypeNode)
		  return 1+((PointerTypeNode)type).getDerefLevel();
	  else
		  return 0;
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  //TODO Generare il codice
  public String codeGeneration() {
		return "";
  }

    
}  