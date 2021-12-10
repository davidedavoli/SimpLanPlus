package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.STentry;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class LhsExpNode extends ExpNode implements Dereferences {

    protected LhsExpNode inner;

    public LhsExpNode (LhsExpNode i) {
	  inner=i;
  }

    @Override
    public Effect getIdStatus(int j) {
        return this.inner.getIdStatus(j);
    }
    public String getID() {
        return inner.getID();
    }
    public LhsExpNode getInner() {
        return inner.getInner();
    }
    public int getDereferenceLevel(){
        if (inner!=null)
            return 1+inner.getDereferenceLevel();
        else
            return 0;
    }
    public STentry getEntry(){
        if (inner!=null)
            return inner.getEntry();
        else
            return null;
    }

    public void setEntry(STentry entry){
        inner.setEntry(entry);
    }


    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<>(inner.checkSemantics(env));
    }

    public TypeNode typeCheck() {
        if (inner != null) return inner.typeCheck().dereference();
        else return null;
    }
    public HasReturn retTypeCheck() {
        return new HasReturn(HasReturn.hasReturnType.ABS);
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        /**
         * Getting new entry if it was modified from some operation on environments
         */
        String id = inner.getID();
        STentry actualEntry = env.effectsLookUp(id);
        inner.setEntry(actualEntry);

        ArrayList<EffectError> errors = new ArrayList<>(inner.checkEffects(env));

        STentry innerEntry = getEntry();
        Effect actualStatus = innerEntry.getDereferenceLevelVariableStatus(getDereferenceLevel()-1);

        if (actualStatus.equals(new Effect(Effect.INITIALIZED))) {
            this.getEntry().setDereferenceLevelVariableStatus(Effect.READWRITE, this.getEntry().getMaxDereferenceLevel()-this.getDereferenceLevel());
            //errors.add(new EffectError(this.getID() + " used before writing value. LhsExpNode"));
        }
        for(int i=0;i<innerEntry.getMaxDereferenceLevel();i++){
            Effect status = innerEntry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new EffectError(inner.getID() + " used after deleting."));
            }
        }
        errors.addAll(checkExpStatus(env));

        return errors;
    }

    public String codeGeneration(Label labelManager) {
        /**
         * Put in $a0 value pointed by inner
         */
        StringBuilder codeGenerated = new StringBuilder();
        codeGenerated.append(inner.codeGeneration(labelManager)).append("\n");
        codeGenerated.append("lw $a0 0($a0)");
        return codeGenerated.toString();
    }

    public List<Dereferences> variables() {
        List<Dereferences> variable = new ArrayList<>();
        variable.add(this);
        return variable;
    }

    public Boolean isPointer() {
        return inner.isPointer();
    }

    public String toPrint(String s) {
        return s+"lhs: " + this.getDereferenceLevel()+" "+this.getID()+"\n";
    }
}  