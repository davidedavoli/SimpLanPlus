package ast.node.statements;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.STentry;
import ast.node.MetaNode;
import ast.node.exp.ExpNode;
import ast.node.exp.IdExpNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class DeletionNode extends MetaNode {

  private final IdExpNode id;
  private TypeNode type;

  public DeletionNode (IdExpNode i) {
    id=i;
    type=null;
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
    //create result list
    ArrayList<SemanticError> res;
    res = id.checkSemantics(env);
    if(res.size() == 0)
      type = id.typeCheck();
    return res;
  }

  public TypeNode typeCheck() {
    if (! (type instanceof PointerTypeNode)){
      System.err.println("Attempt to delete a non pointer variable: "+id.getID());
      System.exit(0);
    }
    return id.typeCheck();
  }
  public HasReturn retTypeCheck() {
    return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  @Override
  public ArrayList<EffectError> checkEffects (Environment env) {

    ArrayList<EffectError> errors = new ArrayList<>(id.checkEffects(env));

    STentry idEntry = id.getEntry();
    if (
            idEntry.getDereferenceLevelVariableStatus(0).equals(new Effect(Effect.DELETED))
              ||
            idEntry.getDereferenceLevelVariableStatus(0).equals(new Effect(Effect.ERROR))
    ) {
      errors.add(new EffectError(id.getID() + " used after deleting."));
    } else {
      errors.addAll(env.checkStmStatus(
              id,
              Effect::sequenceEffect,
              Effect.DELETED)
      );

      idEntry.reInitVariableStatus();
    }

    return errors;
  }

  @Override
  public List<Dereferences> variables() {
    return id.variables();
  }

  public String codeGeneration(Label labelManager) {
    StringBuilder codeGenerated = new StringBuilder();
    codeGenerated.append(id.codeGeneration(labelManager)).append("\n");
    codeGenerated.append("free $a0 //free address in $a0\n");

    return codeGenerated.toString();
  }

  public String toPrint(String s) {
    return s+"Delete:" + id +"\n";
  }
}  