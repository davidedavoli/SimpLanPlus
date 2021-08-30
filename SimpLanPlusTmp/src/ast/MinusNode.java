package ast;

import java.util.ArrayList;

import types.IntTypeNode;
import types.RetEffType;
import types.TypeNode;
import util.SimpLanlib;
import util.Environment;
import util.SemanticError;

public class MinusNode implements Node {

	 private Node left;
	 private Node right;
	  
	 public MinusNode (Node l, Node r) {
	    left=l;
	    right=r;
	 }
	  
	  @Override
	 	public ArrayList<SemanticError> checkSemantics(Environment env) {
		  //create the result
		  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		  
		  //check semantics in the left and in the right exp
		  
		  res.addAll(left.checkSemantics(env));
		  res.addAll(right.checkSemantics(env));
		  
	 	  return res;
	 	}
	  
	  public String toPrint(String s) {
	    return s+"Subt\n" + left.toPrint(s+"  ")  
	                      + right.toPrint(s+"  ") ; 
	  }
	  
	  public TypeNode typeCheck() {
	    if (! ( SimpLanlib.isSubtype(left.typeCheck(),new IntTypeNode()) &&
	            SimpLanlib.isSubtype(right.typeCheck(),new IntTypeNode()) ) ) {
	      System.out.println("Non integers in sub");
	      System.exit(0);
	    }
	    return new IntTypeNode();
	  }
	  
	  public RetEffType retTypeCheck() {
		  return new RetEffType(RetEffType.RetT.ABS);
	  }
	  
	  public String codeGeneration() {
			return left.codeGeneration()+
				   right.codeGeneration()+
				   "sub\n";
	  }
	  
}