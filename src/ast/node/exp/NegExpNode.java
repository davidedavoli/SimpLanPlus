package ast.node.exp;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.IntTypeNode;
import ast.node.types.TypeUtils;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;

public class NegExpNode implements Node {

  private Node exp;
  
  public NegExpNode (Node e) {
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
    return "neg " + exp.toPrint(s);
    }
  
  public TypeNode typeCheck() {
	  TypeNode toret=null;
	  if (! TypeUtils.isSubtype(exp.typeCheck(),new IntTypeNode())) {
		  	  System.out.println("Non int opposite");
			  System.exit(0);
			  toret=new IntTypeNode();
			}
	  return toret;  
  }

	@Override
	public String codeGeneration(Label labelManager) {
		StringBuilder cgen = new StringBuilder();
		String loaded_exp = exp.codeGeneration(labelManager);
		cgen.append(loaded_exp).append("\n");
		cgen.append("multi $a0 $a0 -1 //do negate\n");
		return cgen.toString();
	}

	public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

}  