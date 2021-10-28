package ast.node.exp.single_exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.node.LhsNode;
import ast.node.Node;
  import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.types.BoolTypeNode;
  import ast.node.types.RetEffType;
  import ast.node.types.TypeNode;
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

  public RetEffType retTypeCheck(FunNode funNode) {
    return new RetEffType(RetEffType.RetT.ABS);
  }

  @Override
  public ArrayList<SemanticError> checkEffects(Environment env) {
    return new ArrayList<>();
  }

  @Override
  public List<Dereferenceable> variables() {
    return new ArrayList<Dereferenceable>();
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