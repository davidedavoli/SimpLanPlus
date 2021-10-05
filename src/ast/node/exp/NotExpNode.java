package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.BoolTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class NotExpNode implements Node {

  private Node exp;
  
  public NotExpNode (Node e) {
    exp=e;
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the left and in the right exp
	  
	  res.addAll(exp.checkSemantics(env));
	  
 	  return res;
 	}
  
  public String toPrint(String s) {
    return "not " + exp.toPrint(s);
    }
  
  public TypeNode typeCheck() {
	  TypeNode expType = exp.typeCheck();

	  if (! (expType instanceof BoolTypeNode)){
		  System.out.println("Exp not bool, throw exception");
	  }
	  return new BoolTypeNode();

  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
	  StringBuilder cgen = new StringBuilder();
	  String loaded_exp = exp.codeGeneration(labelManager);
	  cgen.append(loaded_exp);
	  cgen.append("not $a0 $a0\n");

	  return cgen.toString();

  }
}  