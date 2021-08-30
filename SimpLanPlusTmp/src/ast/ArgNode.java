package ast;

import java.util.ArrayList;

import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class ArgNode implements Node {

  private String id;
  private TypeNode type;
  
  public ArgNode (String i, TypeNode t) {
   id=i;
   type=t;
  }
  
  public String getId(){
	  return id;
  }
  
  public TypeNode getType(){
	  return type;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

	  return new ArrayList<SemanticError>();
	}
  
  public String toPrint(String s) {
	  return s+"Par:" + id +"\n"
			 +type.toPrint(s+"  ") ; 
  }
  
  //non utilizzato
  public TypeNode typeCheck () {
     return null;
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  //non utilizzato
  public String codeGeneration() {
		return "";
  }
    
}  