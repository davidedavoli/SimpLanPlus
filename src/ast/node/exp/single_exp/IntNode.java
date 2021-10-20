package ast.node.exp.single_exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.types.IntTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IntNode extends ExpNode {

  private Integer val;
  
  public IntNode (Integer n) {
    val=n;
  }
  
  public String toPrint(String s) {
    return s+"Int:" + Integer.toString(val) +"\n";  
  }
  
  public TypeNode typeCheck() {
    return new IntTypeNode();
  } 
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

  @Override
  public ArrayList<SemanticError> checkEffects(Environment env) {
    return new ArrayList<>();
  }

  public String codeGeneration(Label labelManager){

      StringBuilder cgen = new StringBuilder();
      cgen.append("li $a0 ").append(val).append("\n");
      return cgen.toString();
  }

}  