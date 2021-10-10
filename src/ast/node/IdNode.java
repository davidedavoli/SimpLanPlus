package ast.node;

import java.util.ArrayList;

import ast.STentry;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;

public class IdNode extends LhsNode {

  private String id;
  private STentry entry;
  private int nestinglevel;
  
  public IdNode (String i) {
	  super(null);
	  id=i;
  }
  
  @Override
  public String getID(){
	  return this.id;
  }

  @Override
  public LhsNode getInner(){
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

  public String toPrint(String s) {
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
  
  @Override
  public TypeNode typeCheck () {
	if (entry.getType() instanceof ArrowTypeNode) { //
	  System.out.println("Wrong usage of function identifier");
      System.exit(0);
    }
    return entry.getType();
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration(Label labelManager) {
      /**
       * ritorna indirizzo di ID nel suo frame
       */
      StringBuilder cgen = new StringBuilder();

      cgen.append("addi $a1 $fp 0 //put in $a1 (al) actual fp\n");

      for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++)
          cgen.append("lw $a1 0($a1) //go up to chain\n");

      cgen.append("addi $a1 $a1 ").append(entry.getOffset()).append(" //put in $a1 address of Id\n");

      return cgen.toString();

  }
}  