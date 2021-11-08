package ast.node;

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
  
  public int getDerefLevel() {
	  return 0;
  }
  
  public int getNestingLevel() {
	  return nestinglevel;
  }

  public void setEntry(STentry entry) {
      System.out.println("ST ENTRY SET IN ID NODE");
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
	if (entry.getType() instanceof ArrowTypeNode) { //
	  System.out.println("Wrong usage of function identifier");
      System.exit(0);
    }
    return entry.getType();
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
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
    public ArrayList<SemanticError> checkSemantics(Environment env) {

        //create result list
        ArrayList<SemanticError> res = new ArrayList<SemanticError>();
        entry = env.lookUp(id);
        if (entry == null)
            res.add(new SemanticError("Id "+id+" not declared"));
        else
            nestinglevel = env.getNestingLevel();
        return res;
    }
    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        entry = env.lookUp(id);
        nestinglevel = env.getNestingLevel();;
        return new ArrayList<>();
    }

    public void setStatus(Effect effect, int dereferenceLevel){
        entry.setStatus(effect, dereferenceLevel);
    }
    public Effect getStatus(int dereferenceLevel){
        return entry.getStatus(dereferenceLevel);
    }
}