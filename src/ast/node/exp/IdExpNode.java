package ast.node.exp;

import java.util.ArrayList;

import ast.Dereferenceable;
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
import semantic.SimplanPlusException;

public class IdExpNode extends LhsExpNode implements Dereferenceable {

  private String id;
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
  
  public int detDerefLevel() {
	  return 0;
  }
  
  public int getNestingLevel() {
	  return nestinglevel;
  }
  
  public STentry getEntry() {
	  return entry;
  }
    public void setEntry(STentry entry) {
        this.entry = entry;
    }

  public String toPrint(String s) throws SimplanPlusException {
	return s+"Id:" + id + " at nestlev " + nestinglevel +"\n" + entry.toPrint(s+"  ") ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
	  
	  //create result list
	  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
      entry = env.lookUp(id);
      if (entry == null)
          res.add(new SemanticError("Id "+id+" in expNode not declared"));
      else
          nestinglevel = env.getNestingLevel();

	  return res;
	}
  
  public TypeNode typeCheck () throws SimplanPlusException {
	if (entry.getType() instanceof ArrowTypeNode) { //
	  throw new SimplanPlusException("Wrong usage of function identifier");
     // System.exit(0);
    }
    return entry.getType();
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Ritorna valore di ID
       */

      StringBuilder cgen = new StringBuilder();

      cgen.append("mv $fp $al //put in $al actual fp\n");

      for (int i = 0; i<nestinglevel-entry.getNestingLevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("lw $a0 ").append(entry.getOffset()).append("($al) //put in $a0 value of Id ").append(id).append("\n");

      return cgen.toString();
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        entry = env.effectsLookUp(id);

        Effect actualStatus = entry.getDereferenceLevelVariableStatus(getDereferenceLevel());
        System.out.println("ID exp status "+entry.getStatusList());
        System.out.println("ID exp status "+getDereferenceLevel());
        if (actualStatus.equals(Effect.INITIALIZED)) {
            errors.add(new EffectError(this.getID() + " used before writing value. IdExpNode"));
        }
        //System.out.println("id: "+id+" "+entry.getStatusList());
        errors.addAll(checkExpStatus(env));
        for(int i=0;i<entry.getMaxDereferenceLevel();i++){
            Effect status = entry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new EffectError(this.getID() + " used after deliting. IdExpNode"));
            }
        }

        return errors;
    }
}  