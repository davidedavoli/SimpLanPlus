package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.IntTypeNode;
import ast.node.types.TypeUtils;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class NegExpNode extends ExpNode {

  private ExpNode exp;
  
  public NegExpNode (ExpNode e) {
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
    return "neg " + exp.toPrint(s);
    }
  
  public TypeNode typeCheck() throws SimplanPlusException {
	  if (! TypeUtils.isSubtype(exp.typeCheck(),new IntTypeNode())) 
		  	  throw new SimplanPlusException("Non int negate");
	  return new IntTypeNode();
  }

	@Override
	public String codeGeneration(Label labelManager) throws SimplanPlusException {
		StringBuilder cgen = new StringBuilder();
		String loaded_exp = exp.codeGeneration(labelManager);
		cgen.append(loaded_exp).append("\n");
		cgen.append("multi $a0 $a0 -1 //do negate\n");
		return cgen.toString();
	}

	public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

	@Override
	public List<Dereferenceable> variables() {
		return exp.variables();
	}

	@Override
	public ArrayList<SemanticError> checkEffects(Environment env) {
		ArrayList<SemanticError> errors = new ArrayList<>();
		errors.addAll(exp.checkEffects(env));
		errors.addAll(checkExpStatus(env));
		return errors;
	}

}  