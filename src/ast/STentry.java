package ast;

import ast.node.dec.FunNode;
import ast.node.types.*;
import effect.Effect;
import semantic.Environment;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

public class STentry {

  private final int nestingLevel;
  private TypeNode type;
  private final int offset;

  // status of variable & return & parameter
  private final List<Effect> variableStatus;
//  private final List<Effect> returnStatus;
  private final List<List<Effect>> parametersStatus;

  //Fun entry
  private String beginFuncLabel = "";
  private String endFuncLabel = "";
  private FunNode funNode;

  public STentry (int nestingLevel, TypeNode type, int offset)  {
    this.nestingLevel = nestingLevel;
    this.type = type;
    this.offset = offset;
    this.variableStatus = new ArrayList<>();
    //this.returnStatus = new ArrayList<>();
    this.parametersStatus = new ArrayList<>();
    //Effect initEffect = new Effect();

    if (type instanceof ArrowTypeNode) {

      for (var param : ((ArrowTypeNode) type).getParList()) {
        List<Effect> paramStatus = new ArrayList<>();
        //Check if is right to return 1 in node or to let it call the subclass method
        int numberOfDereference = param.getDereferenceLevel();
        for (int i = 0; i <= numberOfDereference; i++) {
          paramStatus.add(new Effect(Effect.INITIALIZED));
        }
        this.parametersStatus.add(paramStatus);
      }
      /*
      TypeNode returnType = ((ArrowTypeNode) type).getRet();
      if(returnType.getClass() == PointerTypeNode.class){
        int maxReturnLen = ((ArrowTypeNode) type).getRet().getDereferenceLevel();
        for(int index=0; index <= maxReturnLen; index++ ){
          this.returnStatus.add(new Effect(Effect.INITIALIZED));
        }
      }
      else{
        this.returnStatus.add(new Effect(Effect.INITIALIZED));
      }
      */



    }
    else{
      int deferenceLevel = type.getDereferenceLevel();
      for(int g=0; g <= deferenceLevel;g++){
        this.variableStatus.add(new Effect(Effect.INITIALIZED));
      }
    }

  }

  public STentry (int nestingLevel, int offset, String bFL, String eFL,TypeNode type) {
    this (nestingLevel, type, offset);
    beginFuncLabel = bFL;
    endFuncLabel = eFL;
  }

  public STentry(STentry entry) {
    this.nestingLevel = entry.getNestingLevel();
    this.offset = entry.getOffset();
    this.type = entry.getType();
    this.variableStatus = new ArrayList<>();
    //this.returnStatus = new ArrayList<>();
    this.parametersStatus = new ArrayList<>();

    for (var fnStatus : entry.parametersStatus) {
      List<Effect> parameterListStatus = new ArrayList<>();
      for (var status : fnStatus) {
        parameterListStatus.add(new Effect(status));
      }
      this.parametersStatus.add(parameterListStatus);
    }

    for (var status : entry.variableStatus) {
      this.variableStatus.add(new Effect(status));
    }
    this.funNode = entry.funNode;
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
  public void reInitVariableStatus(Environment env) {
    setDereferenceLevelVariableStatus(new Effect(Effect.DELETED), 0);
    updatePointerStatusReference(new Effect(Effect.DELETED), 0);
    for (int i = 1; i < variableStatus.size(); i++) {
      setDereferenceLevelVariableStatus(new Effect(Effect.INITIALIZED), i);
      updatePointerStatusReference(new Effect(Effect.INITIALIZED), i);
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


  public void setFunctionNode(FunNode funNode) {
    this.funNode = funNode;
  }
  public FunNode getFunctionNode() {
    return this.funNode;
  }

  public List<List<Effect>> getFunctionStatusList() {
    return this.parametersStatus;
  }

  public void setParameterStatus(int parameterIndex, Effect effect, int dereferenceLevel) {
    this.parametersStatus.get(parameterIndex).set(dereferenceLevel, new Effect(effect));
  }

  @Override
  public String toString() {
    return "STentry{" +
            "\n\t\tnestingLevel=" + nestingLevel +
            ", \n\t\ttype=" + type +
            ", \n\t\toffset=" + offset +
            ", \n\t\tvariableStatus=" + variableStatus +
            ", \n\t\tparametersStatus=" + parametersStatus +
            //", \n\t\treturnStatus=" + returnStatus +
            ", \n\t\tbeginFuncLabel='" + beginFuncLabel + '\'' +
            ", \n\t\tendFuncLabel='" + endFuncLabel + '\'' +
            ", \n\t\tfunNode=" + funNode +
            "\n\t}";
  }

  public void setBeginLabel(String beginFuncLabel) {
    this.beginFuncLabel = beginFuncLabel;
  }

  public void setEndLabel(String endFuncLabel) {
    this.endFuncLabel = endFuncLabel;
  }

  /*public void setResultList(List<Effect> resultList) {
    TypeNode returnType = ((ArrowTypeNode) type).getRet();

    if((this.returnStatus.size()==0) && (type instanceof ArrowTypeNode) && (returnType.getClass() != PointerTypeNode.class)){ //!(((ArrowTypeNode) type).getRet() instanceof VoidTypeNode)){
      this.returnStatus.add(Effect.READWRITE);
    }
    else {
      this.returnStatus.clear();
      for (int i = 0; i < resultList.size(); i++) {
        this.returnStatus.add(resultList.get(i));
        this.updatePointerStatusReferenceResultList(resultList.get(i), i);
      }
    }
  }

  public void updatePointerStatusReferenceResultList(Effect effect, int dereferenceLevel) {
    this.returnStatus.set(dereferenceLevel, effect);
  }

  public Effect getDereferenceLevelVariableStatusReturn(int dereferenceLevel) {
    return this.returnStatus.get(dereferenceLevel);

  }


  public List<Effect> getReturnList() {
    return this.returnStatus;
  }
*/

}