package ast.node;

import java.util.ArrayList;

import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class ArgNode extends MetaNode {

  private IdNode id;
  private TypeNode type;
  
  public ArgNode (IdNode i, TypeNode t) {
   id=i;
   type=t;
  }
  
  //public String getId(){
//	  return id;
//  }
  
  public TypeNode getType(){
	  return type;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {

	  return new ArrayList<SemanticError>();
	}
  
  public String toPrint(String s) {
	  return s+"Par:" + id +"\n"
			 +type.toPrint(s+"  ") ; 
  }

    //non utilizzato
  public TypeNode typeCheck() {
     return null;
  }
  
  public HasReturn retTypeCheck() {
	  return new HasReturn(HasReturn.hasReturnType.ABS);
  }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        return new ArrayList<>();
    }

    //non utilizzato
  public String codeGeneration(Label labelManager){
		return "";
  }

    public IdNode getIdNode() {
      return this.id;
    }
}  