package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.STentry;
import ast.node.LhsNode;
import ast.node.dec.FunNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class LhsExpNode extends ExpNode implements Dereferenceable {

    protected LhsExpNode inner;

  public LhsExpNode (LhsExpNode i) {
	  inner=i;
  }


    public String getID() {
	  return inner.getID();
  }

  
  /*Non dovrebbe essere fatto così.
  public LhsExpNode getInner() {
	  return inner.getInner();
  }
  */
  public Boolean isPointer() {
      return inner.isPointer();
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
  public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
  		//create result list
  		ArrayList<SemanticError> res = new ArrayList<SemanticError>();
  		res.addAll(inner.checkSemantics(env));
        return res;
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
  
  public HasReturn retTypeCheck(FunNode funNode) {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }
    public List<Dereferenceable> variables() {
        List<Dereferenceable> variable = new ArrayList<>();

        variable.add(this);

        return variable;
    }
    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        /**
         * Getting new entry if it was modified from some operation on environments
         */
        String id = inner.getID();
        STentry actualEntry = env.effectsLookUp(id);
        inner.setEntry(actualEntry);

        errors.addAll(inner.checkEffects(env));

        STentry innerEntry = getEntry();
        Effect actualStatus = innerEntry.getDereferenceLevelVariableStatus(getDereferenceLevel()-1);

        if (actualStatus.equals(new Effect(Effect.INITIALIZED))) {
            errors.add(new EffectError(inner.getID() + " used before writing value. LhsExpNode"));
        }
        for(int i=0;i<innerEntry.getMaxDereferenceLevel();i++){
            Effect status = innerEntry.getDereferenceLevelVariableStatus(i);
            if (status.equals(new Effect(Effect.DELETED))) {
                errors.add(new EffectError(inner.getID() + " used after deleting. LhsExpNode"));
            }
        }
        errors.addAll(checkExpStatus(env));

        return errors;
    }

    public String codeGeneration(Label labelManager) throws SimplanPlusException {
      /**
       * Mette in $a0 quello che c'è nella cella di memoria del puntatore
       */
      StringBuilder cgen = new StringBuilder();
      cgen.append(inner.codeGeneration(labelManager)).append("\n");
      cgen.append("lw $a0 0($a0)");

      return cgen.toString();
  }  
    
}  