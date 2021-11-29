package ast.node;

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

public class IdNode extends LhsNode implements Dereferenceable {

  private String id;
  private STentry entry;
  private int nestinglevel;
  
  public IdNode (String i) {
	  super(null);
	  id=i;
  }
  
  @Override
  public String getID(){
	  return this.id;
  }

  @Override
  public LhsNode getInner(){
	  return this;
  }
  
  public int getDereferenceLevel() {
	  return 0;
  }
  
  public int getNestingLevel() {
	  return nestinglevel;
  }

  public void setEntry(STentry entry) {
      this.entry = entry;
  }
  public STentry getEntry() {
	  return entry;
  }

  public String toPrint(String s) throws SimplanPlusException {
	return s+"Id:" + id + " at nestlev " + nestinglevel +"\n" + entry.toPrint(s+"  ") ;  
  }
  

  
  @Override
  public TypeNode typeCheck () {
      if(entry == null){
          System.out.println("TypeCheck: Entry of identifier "+ id +" is null");
          System.exit(0);
      }
	if (entry.getType() instanceof ArrowTypeNode) { //
	  System.out.println("Wrong usage of function identifier");
      System.exit(0);
    }
    return entry.getType();
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }
  
  public String codeGeneration(Label labelManager){
      /**
       * ritorna indirizzo di ID nel suo frame
       */

      StringBuilder cgen = new StringBuilder();

      cgen.append("mv $fp $al //put in $a1 (al) actual fp\n");

      for (int i = 0; i<nestinglevel-entry.getNestingLevel(); i++)
          cgen.append("lw $al 0($al) //go up to chain\n");

      cgen.append("addi $al $al ").append(entry.getOffset()).append(" //put in $al address of Id\n");

      return cgen.toString();

  }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {

        //create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        entry = env.lookUp(id);
        if (entry == null)
            res.add(new SemanticError("Id "+id+" not declared in idNode"));
        else
            nestinglevel = env.getNestingLevel();
        return res;
    }
    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        entry = env.effectsLookUp(id);
        nestinglevel = env.getNestingLevel();
        return new ArrayList<EffectError>();
    }

    public void setIdStatus(Effect effect, int dereferenceLevel){
        this.entry.updatePointerStatusReference(effect, dereferenceLevel);
    }

    public void declaredIdStatus(Effect effect, int dereferenceLevel){
        this.entry.setDereferenceLevelVariableStatus(effect, dereferenceLevel);
    }
    public Effect getIdStatus(int dereferenceLevel){
        return this.entry.getDereferenceLevelVariableStatus(dereferenceLevel);
    }
    public Boolean isPointer() {
        return entry.getType() instanceof PointerTypeNode;
    }
}