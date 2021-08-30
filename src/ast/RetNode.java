package ast;

import java.util.ArrayList;

import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;
import util.SimpLanlib;

public class RetNode implements Node {

  private Node val;
  private TypeNode etype;// expected type
  
  public RetNode (Node v, TypeNode e) {
    val=v;
    etype=e;
  }
  
  
  public TypeNode getEtype() {
	  return etype;
  }

  public String toPrint(String s) {
    return s+"Return\n" + val.toPrint(s+"  ") ;
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  ArrayList<SemanticError> res = new ArrayList<SemanticError> ();

	  if (! (etype instanceof TypeNode))
          res.add(new SemanticError("Return expression out of a function"));

	  res.addAll(val.checkSemantics(env));
	  return res;
 	}

  
  public TypeNode typeCheck() {
    if (SimpLanlib.isSubtype(val.typeCheck(), etype))
    	return etype;
    else{
        System.out.println("Wrong return type for function");
        System.exit(0);
    	return null;
    }
  }  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.PRES);
  }
    
  public String codeGeneration() {
		return val.codeGeneration()+"";//TODO Codice
  }
    
}  