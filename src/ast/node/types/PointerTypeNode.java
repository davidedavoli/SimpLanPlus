package ast.node.types;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class PointerTypeNode implements TypeNode {
  private TypeNode type;
  
  public PointerTypeNode (TypeNode t) {
	  type=t;
  }
  
  public String toPrint(String s) throws SimplanPlusException {
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
  
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  //TODO Generare il codice
  public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  