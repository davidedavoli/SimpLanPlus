package ast.node.exp;

import java.util.ArrayList;

import ast.STentry;
import ast.node.dec.FunNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

public class IdExpNode extends LhsExpNode {

  private String id;
  private STentry entry;
  private int nestinglevel;
  
  public IdExpNode (String i) {
	  super(null);
	  id=i;
  }
  
  @Override
  public String getID(){
	  return this.id;
  }

  @Override
  public LhsExpNode getInner(){
	  return this;
  }
  
  public int detDerefLevel() {
	  return 0;
  }
  
  public int getNestingLevel() {
	  return nestinglevel;
  }
  
  public STentry getEntry() {
	  return entry;
  }

  public String toPrint(String s) throws SimplanPlusException {
	return s+"Id:" + id + " at nestlev " + nestinglevel +"\n" + entry.toPrint(s+"  ") ;  
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	  
	  int j=env.nestingLevel;
	  STentry tmp=null; 
	  while (j>=0 && tmp==null)
		  tmp=(env.symTable.get(j--)).get(id);
      if (tmp==null)
          res.add(new SemanticError("Id "+id+" not declared"));
      else{
    	  		entry = tmp;
    	  		nestinglevel = env.nestingLevel;
      }
	  
	  return res;
	}
  
  public TypeNode typeCheck () throws SimplanPlusException {
	if (entry.getType() instanceof ArrowTypeNode) { //
	  throw new SimplanPlusException("Wrong usage of function identifier");
     // System.exit(0);
    }
    return entry.getType();
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Ritorna valore di ID
       */

      StringBuilder cgen = new StringBuilder();

      cgen.append("mv $fp $al //put in $al actual fp\n");

      for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("lw $a0 ").append(entry.getOffset()).append("($al) //put in $a0 value of Id\n");

      return cgen.toString();
  }
}  