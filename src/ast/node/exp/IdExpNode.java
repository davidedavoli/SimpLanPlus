package ast.node.exp;

import java.util.ArrayList;

import ast.STentry;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

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
      String getAR="";
	  for (int i=0; i<nestinglevel-entry.getNestinglevel(); i++) 
	    	 getAR+="lw\n";
	    return "push "+entry.getOffset()+"\n"+ //metto offset sullo stack
		       "lfp\n"+getAR+ //risalgo la catena statica
			   "add\n"+ 
               "lw\n"; //carico sullo stack il valore all'indirizzo ottenuto

  }
}  