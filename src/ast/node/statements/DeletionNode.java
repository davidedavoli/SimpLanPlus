package ast.node.statements;

import java.util.ArrayList;

//import org.stringtemplate.v4.compiler.STParser.notConditional_return;

import ast.STentry;
import ast.node.IdNode;
import ast.node.MetaNode;
import ast.node.exp.IdExpNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class DeletionNode extends MetaNode {

  private final IdExpNode id;
  private TypeNode type;
  private STentry entry;

  public DeletionNode (IdExpNode i) {
    id=i;
    type=null;
    entry=null;
  }
  
  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  //create result list
      ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      res = id.checkSemantics(env);
      type = id.typeCheck();
      entry = id.getEntry();
      return res;
  }
  
  public String toPrint(String s) {
	return s+"Delete:" + id +"\n";
  }
  
  //valore di ritorno non utilizzato
  public TypeNode typeCheck () throws SimplanPlusException {
	  if (! (type instanceof PointerTypeNode))
          throw new SimplanPlusException("attempted deletion of a non-pointer variable");
    return id.typeCheck();
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
      ArrayList<EffectError> errors = new ArrayList<>();

      errors.addAll(id.checkEffects(env));
      //System.out.println("prima\n"+env);

      STentry idEntry = id.getEntry();
      if (
              idEntry.getDereferenceLevelVariableStatus(0).equals(new Effect(Effect.DELETED))
              ||
              idEntry.getDereferenceLevelVariableStatus(0).equals(new Effect(Effect.ERROR))
      ) {
        errors.add(new EffectError(id.getID() + " already deleted."));
      } else {
        errors.addAll(env.checkStmStatus(
                id,
                Effect::sequenceEffect,
                Effect.DELETED)
        );

        idEntry.reInitVariableStatus(env);
      }

      return errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      StringBuilder cgen = new StringBuilder();
      cgen.append(id.codeGeneration(labelManager)).append("\n");
      cgen.append("free $a0 //free address in $a0\n");

      return cgen.toString();
  }  
    
}  