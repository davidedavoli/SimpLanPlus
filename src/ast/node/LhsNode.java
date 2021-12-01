package ast.node;

import java.util.ArrayList;

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

  public void setEntry(STentry entry) {
      inner.setEntry(entry);
  }
  public String getID() {
      return inner.getID();
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
    public Boolean isPointer() {
        return inner.isPointer();
    }

    @Override
    public Effect getIdStatus(int j) {
        return this.inner.getIdStatus(j);
    }
    
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return inner.checkSemantics(env);
    }

    public String toPrint(String s) {
        return s+"lhs: " + this.getDereferenceLevel()+" "+this.getID()+"\n";
    }

    //valore di ritorno non utilizzato
    public TypeNode typeCheck() {
        if (inner != null) {
            return inner.typeCheck().dereference();
        }
        else //Questo caso non dovrebbe mai verificarsi per l'implementazione di Visitor.
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
        if (!inner.getEntry().getDereferenceLevelVariableStatus(getDereferenceLevel()-1).equals(new Effect(Effect.READWRITE))) {
            errors.add(new EffectError(inner.getID() + " has not all pointer to rw."));
        }
        return  errors;
    }

    public String codeGeneration(Label labelManager) {
      /**
       * Ritorna indirizzo del puntatore
       */

      StringBuilder codeGenerated = new StringBuilder();
      codeGenerated.append(inner.codeGeneration(labelManager));
      codeGenerated.append("lw $al 0($al) //deferencing inner\n");

      return codeGenerated.toString();
    }

    public void setIdStatus(Effect effect, int level){
        this.inner.setIdStatus(effect,level);
    }
}