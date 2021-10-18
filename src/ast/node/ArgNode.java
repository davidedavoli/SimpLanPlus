package ast.node;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

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
  
  public String toPrint(String s) throws SimplanPlusException {
	  return s+"Par:" + id +"\n"
			 +type.toPrint(s+"  ") ; 
  }
  
  //non utilizzato
  public TypeNode typeCheck () {
     return null;
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  //non utilizzato
  public String codeGeneration(Label labelManager){
		return "";
  }
    
}  