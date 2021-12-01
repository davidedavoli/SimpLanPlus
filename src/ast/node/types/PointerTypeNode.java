package ast.node.types;

import java.util.ArrayList;

import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class PointerTypeNode implements TypeNode {
  private final TypeNode type;
  
  public PointerTypeNode (TypeNode t) {
	  type=t;
  }

  public String toString() {
    return type.toString()+"^";
  }

  public String toPrint(String s) {
      return s+"^"+type.toPrint("");
  }
    
  public TypeNode typeCheck() {
    return null;
  }
  
  public TypeNode dereference() {
	    return type;
  }
  
  public int getDereferenceLevel(){
	  if (type instanceof PointerTypeNode)
		  return 1+ type.getDereferenceLevel();
	  else
		  return 1;
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
      return new ArrayList<>();
  }

  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  @Override
  public ArrayList<EffectError> checkEffects (Environment env) {
        return new ArrayList<>();
    }

  public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  