package ast;

import ast.node.types.TypeNode;
import semanticAnalysis.Effect;

import java.util.ArrayList;
import java.util.List;

public class STentry {
 
  private int nestingLevel;
  private TypeNode type;
  private int offset;
  // variable status
  private final List<Effect> variableStatus;
  

   
  public STentry (int nestingLevel, int offset){
    this.nestingLevel = nestingLevel;
    this.offset = offset;
    this.variableStatus = new ArrayList<>();
  }

  public STentry (int nestingLevel, TypeNode type, int offset){
    this(nestingLevel, offset);
    this.type = type;

//    if (type instanceof FunTypeNode) {
//    } else {
      // ID and pointer
      // pointer handling
      this.variableStatus.add(new Effect(Effect._INITIALIZED));
//    }
  }
  
  public void addType (TypeNode t) {type=t;}
  public TypeNode getType () {return type;}
  public int getOffset () {return offset;}
  public int getNestinglevel () {return this.nestingLevel;}

  public List<Effect> getVariableStatus() {
    return variableStatus;
  }

  public Effect getVariableStatus(int dereferenceLevel) {
    return variableStatus.get(dereferenceLevel);
  }

  public int getMaxDereferenceLevel() {
    return variableStatus.size();
  }

  /**
   * Sets the new effect for the entry in the Symbol Table at the given dereference level.
   *
   * @param status           new status for the variable
   * @param dereferenceLevel level in which {@code status} applies.
   */
  public void setVariableStatus(Effect status, int dereferenceLevel) {
    this.variableStatus.set(dereferenceLevel, new Effect(status));
  }

  /**
   * @return the nesting level of the declaration.
   */
  public int getNestingLevel() {
    return nestingLevel;
  }


  // utils
  @Override
  public String toString() {
    return toPrint("");
  }

  public boolean equals(STentry stEntry) {
    if(this.nestingLevel != stEntry.nestingLevel) return false;
    if(this.variableStatus.equals(stEntry.variableStatus)) return false;
    if(this.offset != stEntry.offset) return false;
    if(this.type != stEntry.type) return false;

    return true;
  }

  public String toPrint(String s) { //
	   return s+"STentry: nestlev " + Integer.toString(this.nestingLevel) +"\n"+
			  s+"STentry: type\n" + 
			  type.toPrint(s+"  ") + 
		      s+"STentry: offset " + Integer.toString(offset) + "\n";
  }
}