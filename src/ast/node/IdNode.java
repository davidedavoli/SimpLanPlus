package ast.node;

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

public class IdNode extends LhsNode implements Dereferences {

    private final String id;
    private STentry entry;
    private int nestingLevel;
  
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

    public Effect getIdStatus(int dereferenceLevel){ return this.entry.getDereferenceLevelVariableStatus(dereferenceLevel); }

    public int getDereferenceLevel() {
	  return 0;
  }

    public STentry getEntry() {
        return entry;
    }


    public void setEntry(STentry entry) {
      this.entry = entry;
  }

    public void setIdStatus(Effect effect, int dereferenceLevel){ this.entry.updatePointerStatusReference(effect, dereferenceLevel); }


    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        //create result list
        ArrayList<SemanticError> res = new ArrayList<>();
        entry = env.lookUp(id);
        if (entry == null)
            res.add(new SemanticError("Id "+id+" not declared."));
        else
            nestingLevel = env.getNestingLevel();
        return res;
    }
  
    @Override
    public TypeNode typeCheck() {
        if(entry == null){
          System.err.println("TypeCheck: Entry of identifier "+ id +" is null");
          System.exit(0);
        }
        if (entry.getType() instanceof ArrowTypeNode) { //
            System.err.println("Wrong usage of function identifier");
            System.exit(0);
        }
        return entry.getType();
    }
  
    public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        entry = env.effectsLookUp(id);
        nestingLevel = env.getNestingLevel();

        return errors;
    }

    public String codeGeneration(Label labelManager) {
        /**
         * return address of id
         */

        StringBuilder codeGenerated = new StringBuilder();

        codeGenerated.append("mv $fp $al //put in $a1 (al) actual fp\n");

        codeGenerated.append("lw $al 0($al) //go up to chain\n".repeat(Math.max(0, nestingLevel - entry.getNestingLevel())));

        codeGenerated.append("addi $al $al ").append(entry.getOffset()).append(" //put in $al address of Id\n");

        return codeGenerated.toString();

    }


    public Boolean isPointer() {
        return entry.getType() instanceof PointerTypeNode;
    }
    public String toPrint(String s) {
        return s+"Id:" + id + " at nesting level " + nestingLevel +"\n" + entry.toPrint(s+"  ") ;
    }
}