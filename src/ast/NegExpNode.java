package ast;

import java.util.ArrayList;

import types.IntTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;
import util.SimpLanlib;

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
	  if (! SimpLanlib.isSubtype(exp.typeCheck(),new IntTypeNode())) {
		  	  System.out.println("Non int opposite");
			  System.exit(0);
			  toret=new IntTypeNode();
			}
	  return toret;  
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
	  String toret=exp.codeGeneration();
	  toret +="push 0\n"+
    		  "sub\n";
	  return toret;
  }
}  