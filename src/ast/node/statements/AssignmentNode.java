package ast.node.statements;

import java.util.ArrayList;

import ast.STentry;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class AssignmentNode implements Node {

  private LhsNode lhs;
  private Node exp;

  
  public AssignmentNode (LhsNode l, Node e) {
    lhs=l;
    exp=e;
  }	  

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

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
  
  public String toPrint(String s) throws SimplanPlusException {
	return s+"Var:" + lhs.getID() +"\n"
	  	   +lhs.typeCheck().toPrint(s+"  ")  
           +exp.toPrint(s+"  "); 
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () throws SimplanPlusException {
    if (! (TypeUtils.isSubtype(exp.typeCheck(),lhs.typeCheck())) )
        throw new SimplanPlusException("incompatible value in assignment for variable "+lhs.getID());
    
    return lhs.typeCheck();
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return new ArrayList<>();
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();
      cgen.append(exp.codeGeneration(labelManager)).append("\n");


      //cgen.append("push $a0 // save exp on stack \n");
        cgen.append("//RITORNATO DA CGEN EXP\n");
      cgen.append(lhs.codeGeneration(labelManager)).append("\n");
      //cgen.append("pop $a0 // put in $a0 top of stack \n");

      //$a1 indirizzo di lhs

      cgen.append("sw $a0 0($al) // 0($a1) = $a0 id=exp \n");

      return cgen.toString();
  }

//@Override
//public ArrayList<SemanticError> delTypeCheck(DelEnv env, int nl) {
//	
//	//create result list
//	ArrayList<SemanticError> res = new ArrayList<SemanticError>();
//	
//	res.addAll(exp.delTypeCheck(env, nl));
//	  
//	int dl = lhs.getDerefLevel();
//	
//	
//	return null;
//}  
    
}  