package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.ExpNode;
import ast.node.exp.LhsExpNode;
import ast.node.exp.single_exp.NewNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import semantic.Effect;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class AssignmentNode implements Node {

  private LhsNode lhs;
  private ExpNode exp;

  
  public AssignmentNode (LhsNode l, ExpNode e) {
    lhs=l;
    exp=e;
  }	  

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();

      STentry entry = env.lookUp(lhs.getID());
      if (entry == null)
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
      ArrayList<SemanticError> errors = new ArrayList<>();

      errors.addAll(lhs.checkEffects(env));
      errors.addAll(exp.checkEffects(env));
      if (lhs.getEntry().getDereferenceLevelVariableStatus(lhs.getDereferenceLevel()).equals(new Effect(Effect.ERROR))) {
        errors.addAll(env.checkStmStatus(lhs, Effect::sequenceEffect, Effect.READWRITE));
      }
      else if (exp instanceof LhsExpNode) {

        List<Dereferenceable> rhsPointerList = exp.variables();

        System.out.println("**************************");
        System.out.println(rhsPointerList);
        var rhsPointer = rhsPointerList.get(0);

        int lhsDerefLvl = lhs.getDereferenceLevel();
        int expDerefLvl = rhsPointer.getDereferenceLevel();
        int lhsMaxDerefLvl = lhs.getEntry().getMaxDereferenceLevel();
        List<Effect> rhsStatusList = rhsPointer.getEntry().getStatusList();
        for (int i = lhsDerefLvl, j = expDerefLvl; i < lhsMaxDerefLvl; i++, j++) {
          /*
          if(rhsStatus.equals(new Effect(Effect.DEL))){
            errors.add(new SemanticError("Assigning deleted variable."));
          }*/
          /*Effect status = rhsPointer.getEntry().getStatus(j);//rhsStatusList.get(j);
          lhs.getEntry().setStatus(status,i);*/
          Effect status = rhsPointer.getIdStatus(j);
          lhs.setIdStatus(status,i);
        }
      }
      else if(exp instanceof NewNode) {
        System.out.println("Istance of newNode");
        lhs.getEntry().setDereferenceLevelVariableStatus(new Effect(Effect.READWRITE), 1);

      }
      else { // lhs is not in error status and exp is not a pointer.
        lhs.getEntry().setDereferenceLevelVariableStatus(new Effect(Effect.READWRITE), lhs.getDereferenceLevel());
      }
      System.out.println("ENTRY " + lhs.getID() + " " + lhs.getEntry().getStatusList());
      return errors;
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