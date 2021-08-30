package ast;

import java.util.ArrayList;

import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class LhsExpNode implements Node {

  protected LhsExpNode inner;
  
  public LhsExpNode (LhsExpNode i) {
	  inner=i;
  }
  
  public String getID() {
	  return inner.getID();
  }
  
  /*Non dovrebbe essere fatto così.
  public LhsExpNode getInner() {
	  return inner.getInner();
  }
  */
  
  public LhsExpNode getInner() {
	  return inner;
  }
  
  public int getDerefLevel(){
	  if (inner!=null)
		  return 1+inner.getDerefLevel();
	  else
		  return 0;
  }

  public STentry getEntry(){
	  if (inner!=null)
		  return inner.getEntry();
	  else
		  return null; 
  }
  
  public int getNestingLevel(){
	  if (inner!=null)
		  return inner.getNestingLevel();
	  else
		  return -1; //sarà un valore sensato?
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
  		//create result list
  		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  		res.addAll(inner.checkSemantics(env));
        return res;
  }
  
  public String toPrint(String s) {
	return s+"lhs: " + this.getDerefLevel()+" "+this.getID()+"\n";
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () {
	if (inner != null) {
		return inner.typeCheck().dereference();
	}
	else //Questo caso non dovrebbe mai verificarsi per l'implementazione di Visitor.
		return null;
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
		return ""; //TODO capire cosa ci va messo
  }  
    
}  