package ast.node.exp;

import java.util.ArrayList;

import ast.Dereferences;
import ast.STentry;
import ast.node.types.ArrowTypeNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class IdExpNode extends LhsExpNode implements Dereferences {

  private final String id;
  private STentry entry;
  private int nestinglevel;
  
  public IdExpNode (String i) {
      super(null);
      id=i;
  }

  public Effect getIdStatus(int dereferenceLevel) {
        return this.entry.getDereferenceLevelVariableStatus(dereferenceLevel);
    }

    public Boolean isPointer() {
        return entry.getType() instanceof PointerTypeNode;
    }
  @Override
  public String getID(){
	  return this.id;
  }

  @Override
  public LhsExpNode getInner(){
	  return this;
  }

  public STentry getEntry() {
	  return entry;
  }
    public void setEntry(STentry entry) {
        this.entry = entry;
    }

  public String toPrint(String s) {
	return s+"Id:" + id + " at nestlev " + nestinglevel +"\n" + entry.toPrint(s+"  ") ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<>();
      entry = env.lookUp(id);
      if (entry == null)
          res.add(new SemanticError("Id "+id+" in expNode not declared"));
      else
          nestinglevel = env.getNestingLevel();

	  return res;
	}
  
  public TypeNode typeCheck() {
	if (entry.getType() instanceof ArrowTypeNode) {
        System.err.println("Wrong usage of function identifier");
        System.exit(0);
    }
    return entry.getType();
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  public String codeGeneration(Label labelManager) {
      /**
       * Ritorna valore di ID
       */

      StringBuilder codeGenerated = new StringBuilder();

      codeGenerated.append("mv $fp $al //put in $al actual fp\n");

      codeGenerated.append("lw $al 0($al) //go up to chain\n".repeat(Math.max(0, nestinglevel - entry.getNestingLevel())));

      codeGenerated.append("lw $a0 ").append(entry.getOffset()).append("($al) //put in $a0 value of Id ").append(id).append("\n");

      return codeGenerated.toString();
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        entry = env.effectsLookUp(id);

        Effect actualStatus = entry.getDereferenceLevelVariableStatus(entry.getMaxDereferenceLevel()-1);

        if (actualStatus.equals(Effect.INITIALIZED)) {
            entry.setDereferenceLevelVariableStatus(Effect.READWRITE, entry.getMaxDereferenceLevel()-1);
            //errors.add(new EffectError(this.getID() + " used before writing value. IdExpNode"));
        }
        ArrayList<EffectError> errors = new ArrayList<>(checkExpStatus(env));
        for(int i=0;i<entry.getMaxDereferenceLevel();i++){
            Effect status = entry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new EffectError(this.getID() + " used after deleting."));
            }
        }

        return errors;
    }
}  