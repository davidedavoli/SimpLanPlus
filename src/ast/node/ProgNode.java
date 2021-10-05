package ast.node;

import java.util.ArrayList;

import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class ProgNode implements Node {

  private Node exp;
  
  public ProgNode (Node e) {
    exp=e;
  }
  
  public String toPrint(String s) {
    
    return "Prog\n" + exp.toPrint("  ") ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		
		return exp.checkSemantics(env);
	}
  
  public TypeNode typeCheck() {
    return exp.typeCheck();
  }  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
		return exp.codeGeneration(labelManager)+"halt\n";
  }  
  
}  