package ast.node.types;

import java.util.ArrayList;

import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IntTypeNode implements TypeNode {
  
    public IntTypeNode () { }
  

    public TypeNode dereference() {
        System.err.println("Attempt to dereference an Integer");
        System.exit(0);
        return null;
    }
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<>();
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

    public String codeGeneration(Label labelManager) {
        return "";
    }


    public String toPrint(String s) {
        return s+"int";
    }
}  