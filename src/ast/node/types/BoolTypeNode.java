package ast.node.types;

import java.util.ArrayList;

import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class BoolTypeNode implements TypeNode {
  
  public BoolTypeNode () {
  }
  
  public String toPrint(String s) {
	return s+"bool";
  }
  
  public TypeNode dereference() {
      System.err.println("Attempt to dereference a Boolean");
      System.exit(0);
      return null;
  }
    
  public TypeNode typeCheck() {
    return null;
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  @Override
  public ArrayList<EffectError> checkEffects (Environment env) {
        return new ArrayList<>();
    }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
     return new ArrayList<>();
  }

   public String codeGeneration(Label labelManager) {
		return "";
  }

    
}  