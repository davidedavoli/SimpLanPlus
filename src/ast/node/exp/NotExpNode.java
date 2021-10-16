package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.BoolTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

public class NotExpNode implements Node {

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
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {
	  StringBuilder cgen = new StringBuilder();
	  String loaded_exp = exp.codeGeneration(labelManager);
	  cgen.append(loaded_exp).append("\n");
	  cgen.append("not $a0 $a0\n");

	  return cgen.toString();

  }
}  