package ast.node.exp.single_exp;

import java.util.ArrayList;
import java.util.List;

import GraphEffects.EffectsManager;
import ast.Dereferenceable;
import ast.node.exp.ExpNode;
import ast.node.types.BoolTypeNode;
  import ast.node.types.HasReturn;
  import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
  import ast.Label;
  import semantic.SemanticError;

public class BoolNode extends ExpNode {

  private boolean val;

  public BoolNode (boolean n) {
  val=n;
  }

  public String toPrint(String s) {
    if (val) return s+"Bool:true\n";
    else return s+"Bool:false\n";
  }

  public TypeNode typeCheck() {
  return new BoolTypeNode();
  }

  public HasReturn retTypeCheck() {
    return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  @Override
  public ArrayList<EffectError> checkEffects (Environment env) {
    return new ArrayList<>();
  }

  @Override
  public List<Dereferenceable> variables() {
    return new ArrayList<Dereferenceable>();
  }

  @Override
  public void readGraphEffect(EffectsManager m) {

  }

  @Override
  public void checkGraphEffects(EffectsManager m) {

  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {

    return new ArrayList<SemanticError>();
  }

  public String codeGeneration(Label labelManager){
    StringBuilder cgen = new StringBuilder();
    cgen.append("li $a0 ").append(val?1:0).append("\n");
    return cgen.toString();
  }
}