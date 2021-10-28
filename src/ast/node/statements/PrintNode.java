package ast.node.statements;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class PrintNode implements Node {

  private Node val;
  
  public PrintNode (Node v) {
    val=v;
  }
  
  public String toPrint(String s) throws SimplanPlusException {
    return s+"Print\n" + val.toPrint(s+"  ") ;
  }
  
  public TypeNode typeCheck() throws SimplanPlusException {
    return val.typeCheck();
  }  
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

 	  return val.checkSemantics(env);
 	}
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
      ArrayList<SemanticError> errors = new ArrayList<>();
      errors.addAll(val.checkEffects(env));
      return errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        StringBuilder cgen = new StringBuilder();
        cgen.append(val.codeGeneration(labelManager)).append("\n");
        cgen.append("print $a0\n");
		return cgen.toString();
  }
    
}  