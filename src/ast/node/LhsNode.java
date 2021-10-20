package ast.node;

import java.util.ArrayList;

import ast.STentry;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class LhsNode implements Node {

  protected LhsNode inner;

  public LhsNode (LhsNode i) {
      inner=i;
  }

  public String getID() {
      return inner.getID();
  }

  public LhsNode getInner() {
      return inner.getInner();
  }

  public int getDerefLevel(){
      if (inner!=null)
          return 1+inner.getDerefLevel();
      else
          return 0;
  }

  public STentry getEntry(){
      if (inner!=null)
          return inner.getEntry();
      else
          return null;
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
        return s+"lhs: " + this.getDerefLevel()+" "+this.getID()+"\n";
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
        return null;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Ritorna indirizzo del puntatore
       */

      StringBuilder cgen = new StringBuilder();
      inner.codeGeneration(labelManager);
      cgen.append("lw $al 0($al)");

      return cgen.toString();
    }

}