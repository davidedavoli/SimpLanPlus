package ast.node.exp.single_exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.exp.ExpNode;
import ast.node.types.IntTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IntNode extends ExpNode {

    private final Integer val;

    public IntNode (Integer n) {
      val=n;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
    return new ArrayList<>();
  }

    public TypeNode typeCheck() {
      return new IntTypeNode();
    }
    public HasReturn retTypeCheck() {
        return new HasReturn(HasReturn.hasReturnType.ABS);
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
      return new ArrayList<>();
    }

    public String codeGeneration(Label labelManager) {

        StringBuilder codeGenerated = new StringBuilder();
        codeGenerated.append("li $a0 ").append(val).append("\n");
        return codeGenerated.toString();
    }

    @Override
    public List<Dereferences> variables() {
    return new ArrayList<>();
  }

    public String toPrint(String s) {
    return s+"Int:" + val +"\n";
  }

}  