package ast.node.exp;

import java.util.ArrayList;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.dec.FunNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Effect;
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
        System.out.println("ST ENTRY SET IN IDEXP NODE");
        this.entry = entry;
    }

  public String toPrint(String s) throws SimplanPlusException {
	return s+"Id:" + id + " at nestlev " + nestinglevel +"\n" + entry.toPrint(s+"  ") ;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  
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
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

  public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Ritorna valore di ID
       */

      StringBuilder cgen = new StringBuilder();

      cgen.append("mv $fp $al //put in $al actual fp\n");

      for (int i = 0; i<nestinglevel-entry.getNestingLevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("lw $a0 ").append(entry.getOffset()).append("($al) //put in $a0 value of Id\n");

      return cgen.toString();
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        ArrayList<SemanticError> errors = new ArrayList<>();
        System.out.println(env.getCurrentST());
        entry = env.effectsLookUp(id);

        Effect actualStatus = entry.getDereferenceLevelVariableStatus(getDereferenceLevel());
        if (actualStatus.equals(Effect.INITIALIZED)) {
            errors.add(new SemanticError(this.getID() + " used before writing value. IdExpNode"));
        }
        errors.addAll(checkExpStatus(env));
        for(int i=0;i<entry.getMaxDereferenceLevel();i++){
            Effect status = entry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new SemanticError(this.getID() + " used after deliting. IdExpNode"));
            }
        }

        return errors;
    }
}  