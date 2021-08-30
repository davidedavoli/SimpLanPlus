package ast;

import java.util.ArrayList;
import java.util.HashMap;

import del.DelEnv;
import types.RetEffType;
import types.TypeNode;
import util.Environment;
import util.SemanticError;
import util.SimpLanlib;

public class AssignmentNode implements Node {

  private LhsNode lhs;
  private Node exp;
  
  public AssignmentNode (LhsNode l, Node e) {
    lhs=l;
    exp=e;
  }	  

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {

	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();

	  int j=env.nestingLevel;
	  STentry tmp=null; 
	  while (j>=0 && tmp==null)
		  tmp=(env.symTable.get(j--)).get(lhs.getID());
	  if (tmp==null)
		  res.add(new SemanticError("Id "+lhs.getID()+" not declared"));
	    
	  res.addAll(lhs.checkSemantics(env));
      res.addAll(exp.checkSemantics(env));
        
        return res;
  }
  
  public String toPrint(String s) {
	return s+"Var:" + lhs.getID() +"\n"
	  	   +lhs.typeCheck().toPrint(s+"  ")  
           +exp.toPrint(s+"  "); 
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () {
    if (! (SimpLanlib.isSubtype(exp.typeCheck(),lhs.typeCheck())) ){      
      System.out.println("incompatible value in assignment for variable "+lhs.getID());
      System.exit(0);
    }     
    return lhs.typeCheck();
  }
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }
  
  public String codeGeneration() {
		return exp.codeGeneration();
  }

@Override
public ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl) {
	
	//create result list
	ArrayList<SemanticError> res = new ArrayList<SemanticError>();
	
	res.addAll(exp.delTypeCheck(env, nl));
	  
	int dl = lhs.getDerefLevel();
	
	
	return null;
}  
    
}  