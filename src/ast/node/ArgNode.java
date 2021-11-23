package ast.node;

import java.util.ArrayList;

import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class ArgNode implements Node {

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
  
  public String toPrint(String s) throws SimplanPlusException {
	  return s+"Par:" + id +"\n"
			 +type.toPrint(s+"  ") ; 
  }
  
  //non utilizzato
  public TypeNode typeCheck () {
     return null;
  }
  
  public RetEffType retTypeCheck(FunNode funNode) {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
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