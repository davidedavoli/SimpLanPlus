package ast.node;

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

public class LhsNode extends MetaNode implements Dereferences {

    protected LhsNode inner;

    public LhsNode (LhsNode i) {
          inner=i;
      }


/**
 * =====================================================
 * Getter
 * =====================================================
 **/
    public String getID() {
            return inner.getID();
        }

    @Override
    public Effect getIdStatus(int j) {
        return this.inner.getIdStatus(j);
    }

    public LhsNode getInner() {
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

/**
 * =====================================================
 * Setter
 * =====================================================
 **/
    public void setEntry(STentry entry) {
      inner.setEntry(entry);
    }

    public void setIdStatus(Effect effect, int level){
        this.inner.setIdStatus(effect,level);
    }


    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return inner.checkSemantics(env);
    }

    public TypeNode typeCheck() {
        if (inner != null) {
            return inner.typeCheck().dereference();
        }
        else // Should never happen thanks to Visitor.
            return null;
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
        for(int i=0;i<this.getDereferenceLevel();i++){
            Effect status = actualEntry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new EffectError(this.getID() + " used after deleting."));
            }
        }
        if (!inner.getEntry().getDereferenceLevelVariableStatus(getDereferenceLevel()-1).equals(new Effect(Effect.READWRITE)) && errors.size()==0) {
            errors.add(new EffectError(inner.getID() + " has not all pointer to rw."));
        }
        return  errors;
    }

    public String codeGeneration(Label labelManager) {
      /**
       * Load Pointer address
       */

      StringBuilder codeGenerated = new StringBuilder();
      codeGenerated.append(inner.codeGeneration(labelManager));
      codeGenerated.append("lw $al 0($al) // de referencing inner\n");

      return codeGenerated.toString();
    }


    public Boolean isPointer() {
        return inner.isPointer();
    }

    public String toPrint(String s) {
        return s+"lhs: " + this.getDereferenceLevel()+" "+this.getID()+"\n";
    }

    @Override
    public List<Dereferences> variables() {
        ArrayList<Dereferences> res = new ArrayList<Dereferences>();
        res.add(this);
        return res;
    }
}