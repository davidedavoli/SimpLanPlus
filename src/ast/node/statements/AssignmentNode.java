package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.LhsNode;
import ast.node.MetaNode;
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

public class AssignmentNode extends MetaNode {

  private final LhsNode lhs;
  private final ExpNode exp;


  public AssignmentNode (LhsNode l, ExpNode e) {
    lhs=l;
    exp=e;
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {

    //create result list
    ArrayList<SemanticError> res = new ArrayList<>();

    res.addAll(lhs.checkSemantics(env));
    res.addAll(exp.checkSemantics(env));

    return res;
  }

  public TypeNode typeCheck() {
    if (! (TypeUtils.isSubtype(exp.typeCheck(),lhs.typeCheck())) ){
      System.err.println("Incompatible value in assignment for variable "+lhs.getID() + " of type: "+lhs.typeCheck().toPrint("") + " when exp is of type: "+exp.typeCheck().toPrint(""));
      System.exit(0);
    }
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

      List<Dereferences> rhsPointerList = exp.variables();
      var rhsPointer = rhsPointerList.get(0);

      int lhsDereferenceLevel = lhs.getDereferenceLevel();
      int rhsPointerDereferenceLevel = rhsPointer.getDereferenceLevel();
      int lhsMaxDereferenceLevel = lhs.getEntry().getMaxDereferenceLevel();

      for (int i = lhsDereferenceLevel, j = rhsPointerDereferenceLevel; i < lhsMaxDereferenceLevel; i++, j++) {
        Effect status = rhsPointer.getEntry().getDereferenceLevelVariableStatus(j);
        lhs.setIdStatus(status,i);
      }
    }
      /*else if(exp instanceof CallExpNode){
        STentry returnedEffectEntry = env.effectsLookUp( ((CallExpNode) exp).getIdName());//.innerEntry();
        int lhsDereferenceLevel = lhs.getDereferenceLevel();
        for (int i = lhsDereferenceLevel, j = 0; i < returnedEffectEntry.getReturnList().size(); i++, j++) {
          Effect status = returnedEffectEntry.getDereferenceLevelVariableStatusReturn(j);
          lhs.setIdStatus(status,i);
        }
        env.addEntry(lhs.getID(),lhs.getEntry());
        lhs.setEntry(lhs.getEntry());

        //
      }
      */
    else { // lhs is not in error status and exp is not a pointer.
      lhs.getEntry().setDereferenceLevelVariableStatus(new Effect(Effect.READWRITE), lhs.getDereferenceLevel());

    }
    return errors;
  }

  public String codeGeneration(Label labelManager) {
    StringBuilder codeGenerated = new StringBuilder();
    codeGenerated.append(exp.codeGeneration(labelManager)).append("\n");

    codeGenerated.append(lhs.codeGeneration(labelManager)).append("\n");
    //$al address di lhs
    codeGenerated.append("sw $a0 0($al) // 0($al) = $a0 ").append(lhs.getID()).append("=exp\n");

    return codeGenerated.toString();
  }

  @Override
  public List<Dereferences> variables(){
    List<Dereferences> res = new ArrayList<>();
    res.addAll(exp.variables());
    res.add(lhs);
    return res;
  }

  public String toPrint(String s) {
    return s+"Var:" + lhs.getID() +"\n"
            +lhs.typeCheck().toPrint(s+"  ")
            +exp.toPrint(s+"  ");
  }

}  