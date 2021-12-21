package ast;

import ast.node.dec.FunNode;
import ast.node.types.*;
import effect.Effect;
import java.util.ArrayList;
import java.util.List;

public class STentry {

  private final int nestingLevel;
  private final TypeNode type;
  private final int offset;
  private boolean isPar;

  // status of variable & return & parameter
  private final List<Effect> variableStatus;
//  private final List<Effect> returnStatus;
  private final List<List<Effect>> parametersStatus;

  //Fun entry
  private String beginFuncLabel = "";
  private String endFuncLabel = "";
  private FunNode funNode;

/**
 * =====================================================
 * Constructor
 * =====================================================
 **/

  public STentry (int nestingLevel, TypeNode type, int offset, boolean isPar)  {
    this.nestingLevel = nestingLevel;
    this.type = type;
    this.offset = offset;
    this.isPar = isPar;
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
    this (nestingLevel, type, offset, false);
    beginFuncLabel = bFL;
    endFuncLabel = eFL;
  }

  public STentry(STentry entry) {
    this.nestingLevel = entry.getNestingLevel();
    this.offset = entry.getOffset();
    this.type = entry.getType();
    this.isPar = entry.getisPar();
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
    this.beginFuncLabel = entry.beginFuncLabel;
    this.endFuncLabel = entry.endFuncLabel;
  }

/**
 * =====================================================
 * End Constructor
 * =====================================================
 **/

/**
 * =====================================================
 * Getter
 * =====================================================
 **/
  public String getBeginFuncLabel() {
    return beginFuncLabel;
  }

  public boolean getisPar() {
    return isPar;
  }

  public TypeNode getType () {return this.type;}

  public int getOffset () {return this.offset;}
  
  public int getNestingLevel() {return this.nestingLevel;}

  public Effect getDereferenceLevelVariableStatus(int dereferenceLevel) {
    return this.variableStatus.get(dereferenceLevel);
  }

  public int getMaxDereferenceLevel() {
    return variableStatus.size();
  }

  public FunNode getFunctionNode() {
    return this.funNode;
  }

  public List<List<Effect>> getFunctionStatusList() {
    return this.parametersStatus;
  }

/**
 * =====================================================
 * End Getter
 * =====================================================
 **/


/**
 * =====================================================
 * Setter
 * =====================================================
 **/

  public void setFunctionNode(FunNode funNode) {
    this.funNode = funNode;
  }

  public void setParameterStatus(int parameterIndex, Effect effect, int dereferenceLevel) {
    this.parametersStatus.get(parameterIndex).set(dereferenceLevel, new Effect(effect));
  }

  public void setBeginLabel(String beginFuncLabel) {
    this.beginFuncLabel = beginFuncLabel;
  }

  public void setEndLabel(String endFuncLabel) {
    this.endFuncLabel = endFuncLabel;
  }

/**
 * =====================================================
 * End setter
 * =====================================================
 **/

/**
 * =====================================================
 * Effect status
 * =====================================================
 **/

  /**
   *
   * @param effect Reference to the same effect of the rhs
   * @param dereferenceLevel Dereference level of the lhs
   * Using the same reference of the rhs
  */
  public void updatePointerStatusReference(Effect effect, int dereferenceLevel) {
    this.variableStatus.set(dereferenceLevel, effect);
  }

  /**
   *
   * @param effect Effect from the rhs
   * @param dereferenceLevel Dereference level of the lhs
   * In this case we just update the status, so it will change in every reference.
   */
  public void setDereferenceLevelVariableStatus(Effect effect, int dereferenceLevel) {
    this.variableStatus.get(dereferenceLevel).updateStatus(effect);
  }

  /**
   * After deleting a pointer first update the status so every pointer to him is delete
   * and then reinitialize the effect list to init
   */
  public void reInitVariableStatus() {
    setDereferenceLevelVariableStatus(new Effect(Effect.DELETED), 0);
    updatePointerStatusReference(new Effect(Effect.DELETED), 0);
    for (int i = 1; i < variableStatus.size(); i++) {
      setDereferenceLevelVariableStatus(new Effect(Effect.INITIALIZED), i);
      updatePointerStatusReference(new Effect(Effect.INITIALIZED), i);
    }
  }

/**
 * =====================================================
 * End Effect status
 * =====================================================
 **/

  public String toPrint(String s) { //
	   return s+"ST entry: nesting level " + nestingLevel +"\n"+
			  s+"ST entry: type\n" +
			  type.toPrint(s+"  ") + 
		      s+"ST entry: offset " + offset + "\n";
  }

  @Override
  public String toString() {
    return "ST entry{" +
            "\n\t\tnestingLevel=" + nestingLevel +
            ", \n\t\ttype=" + type +
            ", \n\t\toffset=" + offset +
            ", \n\t\tvariableStatus=" + variableStatus +
            ", \n\t\tparametersStatus=" + parametersStatus +
            //", \n\t\t returnStatus=" + returnStatus +
            ", \n\t\tbeginFuncLabel='" + beginFuncLabel + '\'' +
            ", \n\t\tendFuncLabel='" + endFuncLabel + '\'' +
            ", \n\t\tfunNode=" + funNode +
            "\n\t}";
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