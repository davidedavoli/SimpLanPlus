package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.node.LhsNode;
import ast.node.MetaNode;
import ast.node.dec.FunNode;
import ast.node.exp.CallExpNode;
import ast.node.exp.ExpNode;
import ast.node.exp.LhsExpNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class AssignmentNode extends MetaNode {

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

      //TODO Should not be performed because IDNode and IdExpNode alreay do it
      //STentry entry = env.lookUp(lhs.getID());
      //if (entry == null)
      //    res.add(new SemanticError("Id "+lhs.getID()+" not declared"));

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


  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
      ArrayList<EffectError> errors = new ArrayList<>();

      errors.addAll(lhs.checkEffects(env));
      errors.addAll(exp.checkEffects(env));
      if (lhs.getEntry().getDereferenceLevelVariableStatus(lhs.getDereferenceLevel()).equals(new Effect(Effect.ERROR))) {
        errors.addAll(env.checkStmStatus(lhs, Effect::sequenceEffect, Effect.READWRITE));
      }
      else if (exp instanceof LhsExpNode) {

        List<Dereferenceable> rhsPointerList = exp.variables();
        var rhsPointer = rhsPointerList.get(0);

        int lhsDerefLvl = lhs.getDereferenceLevel();
        int expDerefLvl = rhsPointer.getDereferenceLevel();
        int lhsMaxDerefLvl = lhs.getEntry().getMaxDereferenceLevel();

        for (int i = lhsDerefLvl, j = expDerefLvl; i < lhsMaxDerefLvl; i++, j++) {
          Effect status = rhsPointer.getEntry().getDereferenceLevelVariableStatus(j);
          lhs.setIdStatus(status,i);
        }
      }
      else if(exp instanceof CallExpNode){
        List<Effect> returnedEffectList = ((CallExpNode) exp).innerEntry().getReturnList();

        int lhsDereferenceLevel = lhs.getDereferenceLevel();

        for (int i = lhsDereferenceLevel, j = 0; i < returnedEffectList.size(); i++, j++) {
          Effect status = returnedEffectList.get(j);
          //lhs.setIdStatus(status,i);
          lhs.getEntry().setDereferenceLevelVariableStatus(status,i);

        }
        env.addEntry(lhs.getID(),lhs.getEntry());
        System.out.println(lhs.getEntry().getStatusList());
        //lhs.setEntry(lhs.getEntry());
      }
      else { // lhs is not in error status and exp is not a pointer.
        lhs.getEntry().setDereferenceLevelVariableStatus(new Effect(Effect.READWRITE), lhs.getDereferenceLevel());
      }
      return errors;
    }


  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();
      cgen.append(exp.codeGeneration(labelManager)).append("\n");
      cgen.append("//RITORNATO DA CGEN EXP\n");

      cgen.append(lhs.codeGeneration(labelManager)).append("\n");
      //$al indirizzo di lhs
      cgen.append("sw $a0 0($al) // 0($al) = $a0 ").append(lhs.getID()).append("=exp\n");

      return cgen.toString();
  }

}  