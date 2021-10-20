package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class NotExpNode extends ExpNode {

  private Node exp;
  
  public NotExpNode (Node e) {
    exp=e;
  }
  
  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  //create the result
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  //check semantics in the left and in the right exp
	  
	  res.addAll(exp.checkSemantics(env));
	  
 	  return res;
 	}
  
  public String toPrint(String s) throws SimplanPlusException {
    return "not " + exp.toPrint(s);
    }
  
  public TypeNode typeCheck() throws SimplanPlusException {
	  TypeNode expType = exp.typeCheck();

	  if (! (expType instanceof BoolTypeNode)){
		  throw new SimplanPlusException("Exp not bool, throw exception");
	  }
	  return new BoolTypeNode();

  }
	/*@Override
	public List<LhsNode> variables() {
		return exp.variables();
	}*/
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();
		errors.addAll(exp.checkEffects(env));
		errors.addAll(checkExpStatus(env));
		return errors;
	}

	public String codeGeneration(Label labelManager) throws SimplanPlusException {
	  StringBuilder cgen = new StringBuilder();
	  String loaded_exp = exp.codeGeneration(labelManager);
	  cgen.append(loaded_exp).append("\n");
	  cgen.append("not $a0 $a0\n");

	  return cgen.toString();

  }
}  