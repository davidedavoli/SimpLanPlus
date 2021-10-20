package ast;

import ast.node.types.TypeNode;
import semantic.Effect;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

public class STentry {

  private final int nestingLevel;
  private TypeNode type;
  private final int offset;

  // status of variable
  private List<Effect> variableStatus = new ArrayList<Effect>();

  private String beginFuncLabel = "";
  private String endFuncLabel = "";
   
  public STentry (int nestingLevel, TypeNode type, int offset) {
    this.nestingLevel = nestingLevel;
    this.type = type;
    this.offset = offset;
    Effect initEffect = new Effect();
    this.variableStatus.add(initEffect);
  }

  public STentry (int nestingLevel, int offset, String bFL, String eFL) {
    this.nestingLevel = nestingLevel;
    this.offset = offset;
    beginFuncLabel = bFL;
    endFuncLabel = eFL;
    this.variableStatus = new ArrayList<>();
  }

  public String getBeginFuncLabel() {
    return beginFuncLabel;
  }

  public String getEndFuncLabel() {
    return endFuncLabel;
  }

  public void addType (TypeNode t) {this.type = t;}
  
  public TypeNode getType () {return this.type;}

  public int getOffset () {return this.offset;}
  
  public int getNestinglevel () {return this.nestingLevel;}
  
  public String toPrint(String s) throws SimplanPlusException { //
	   return s+"STentry: nestlev " + Integer.toString(nestingLevel) +"\n"+
			  s+"STentry: type\n" + 
			  type.toPrint(s+"  ") + 
		      s+"STentry: offset " + Integer.toString(offset) + "\n";
  }

  public void setStatus(Effect effect, int dereferenceLevel) {
    this.variableStatus.set(dereferenceLevel, effect);
  }
}  