package ast;

import java.util.ArrayList;

import types.BoolTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;
import util.SimpLanlib;

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
	  TypeNode toret=null;
	  if (! SimpLanlib.isSubtype(exp.typeCheck(),new BoolTypeNode())) {
		  	  System.out.println("Non bool in negation");
			  System.exit(0);
			  toret=new BoolTypeNode();
			}
	  return toret;  
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
	  String toret=exp.codeGeneration();
	  toret +="push 1\n"+
    		  "sub\n";
	  return toret;
  }
}  