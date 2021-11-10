package ast;

import ast.node.types.PointerTypeNode;
import ast.node.types.TypeNode;
import semantic.Effect;
import semantic.SimplanPlusException;
import ast.node.types.TypeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class STentry {

  private final int nestingLevel;
  private TypeNode type;
  private final int offset;

  // status of variable
  private final List<Effect> variableStatus;
  private String beginFuncLabel = "";
  private String endFuncLabel = "";
   
  public STentry (int nestingLevel, TypeNode type, int offset)  {
    this.nestingLevel = nestingLevel;
    this.type = type;
    this.offset = offset;
    this.variableStatus = new ArrayList<>();
    //Effect initEffect = new Effect();
    try{
      int deferenceLevel = ((PointerTypeNode) type).getDerefLevel();
      for(int g=0; g <= deferenceLevel;g++){
        this.variableStatus.add(new Effect(Effect.INITIALIZED));
      }
    }
    catch(Exception e){
      this.variableStatus.add(new Effect(Effect.INITIALIZED));
    }
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
  
  public int getNestingLevel() {return this.nestingLevel;}
  
  public String toPrint(String s) throws SimplanPlusException { //
	   return s+"STentry: nestlev " + nestingLevel +"\n"+
			  s+"STentry: type\n" + 
			  type.toPrint(s+"  ") + 
		      s+"STentry: offset " + offset + "\n";
  }

  public void updatePointerStatusReference(Effect effect, int dereferenceLevel) {
    this.variableStatus.set(dereferenceLevel, effect);
  }

  public void setDereferenceLevelVariableStatus(Effect effect, int dereferenceLevel) {
    this.variableStatus.get(dereferenceLevel).updateStatus(effect);
    //this.variableStatus.set(dereferenceLevel, effect);
  }
  public void reInitVariableStatus() {
    setDereferenceLevelVariableStatus(Effect.DELETED, 0);
    updatePointerStatusReference(Effect.DELETED, 0);
    for (int i = 1; i < variableStatus.size(); i++) {
      setDereferenceLevelVariableStatus(Effect.INITIALIZED, i);
      updatePointerStatusReference(Effect.INITIALIZED, i);
    }
  }

  public Effect getDereferenceLevelVariableStatus(int dereferenceLevel) {
    return this.variableStatus.get(dereferenceLevel);
  }


  public List<Effect> getStatusList() {
    return variableStatus;
  }

  public int getMaxDereferenceLevel() {
    return variableStatus.size();
  }

  public void printStatus() {
    System.out.println(this.variableStatus);
  }

  public void updatePointerStatus(List<Effect> newArrayList,Integer startIndex) {
    List<Effect> newList = new ArrayList<>();
    newList.addAll(this.variableStatus.subList(0,startIndex));
    newList.addAll(newArrayList);
    this.variableStatus.clear();
    this.variableStatus.addAll(newList);
  }


}