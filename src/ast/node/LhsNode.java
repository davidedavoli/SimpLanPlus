package ast.node;

import java.util.ArrayList;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Effect;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class LhsNode implements Node, Dereferenceable {

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

    @Override
    public Effect getIdStatus(int j) {
        return this.inner.getIdStatus(j);
    }

    public int getNestingLevel(){
      if (inner!=null)
          return inner.getNestingLevel();
      else
          return -1; //sarà un valore sensato?
  }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return inner.checkSemantics(env);
    }

    public String toPrint(String s) throws SimplanPlusException {
        return s+"lhs: " + this.getDereferenceLevel()+" "+this.getID()+"\n";
    }

    //valore di ritorno non utilizzato
    public TypeNode typeCheck () throws SimplanPlusException {
        if (inner != null) {
            return inner.typeCheck().dereference();
        }
        else //Questo caso non dovrebbe mai verificarsi per l'implementazione di Visitor.
            return null;
    }

    public RetEffType retTypeCheck(FunNode funNode) {
      return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        ArrayList<SemanticError> errors = new ArrayList<>();
        errors.addAll(inner.checkEffects(env));
        System.out.println("DEREF LEVEL IN LHS " + getDereferenceLevel());
        if (!inner.getEntry().getDereferenceLevelVariableStatus(getDereferenceLevel()-1).equals(new Effect(Effect.READWRITE))) {
            errors.add(new SemanticError(inner.getID() + " has not all pointer to rw "));
        }
        return  errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Ritorna indirizzo del puntatore
       */

      StringBuilder cgen = new StringBuilder();
      cgen.append(inner.codeGeneration(labelManager));
      cgen.append("lw $al 0($al) //deferencing inner\n");

      return cgen.toString();
    }

    public void setIdStatus(Effect effect, int level){
        this.inner.setIdStatus(effect,level);
    }
}