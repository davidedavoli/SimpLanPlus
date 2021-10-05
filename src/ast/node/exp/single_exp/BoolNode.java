package ast.node.exp.single_exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.BoolTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class BoolNode implements Node {

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
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {

 	  return new ArrayList<SemanticError>();
 	}
  
  public String codeGeneration(Label labelManager) {
      StringBuilder cgen = new StringBuilder();
      cgen.append("li $a0 ").append(val?1:0).append("\n");
      return cgen.toString();
	  }
         
}  