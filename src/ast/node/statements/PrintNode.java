package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.MetaNode;
import ast.node.Node;
import ast.node.exp.ExpNode;
import ast.node.exp.IdExpNode;
import ast.node.types.*;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class PrintNode extends MetaNode {

  private final Node val;
  
  public PrintNode (Node v) {
    val=v;
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
    return val.checkSemantics(env);
  }

  public TypeNode typeCheck() {
    TypeNode valType = val.typeCheck();
    if(valType instanceof PointerTypeNode){
      System.err.println("Trying to print a pointer "+((IdExpNode)val).getID());
      System.exit(0);
    }
    return valType;
  }
  public HasReturn retTypeCheck() {
    return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  @Override
  public ArrayList<EffectError> checkEffects (Environment env) {
    return new ArrayList<>(val.checkEffects(env));
  }

  public String codeGeneration(Label labelManager) {
    StringBuilder codeGenerated = new StringBuilder();
    codeGenerated.append(val.codeGeneration(labelManager)).append("\n");
    codeGenerated.append("print $a0\n");
    return codeGenerated.toString();
  }

  @Override
  public List<Dereferences> variables() {
    return ((ExpNode) val).variables();
  }


  public String toPrint(String s) {
    return s+"Print\n" + val.toPrint(s+"  ") ;
  }
}  