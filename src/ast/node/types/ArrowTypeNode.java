package ast.node.types;
import java.util.ArrayList;
import java.util.List;

import ast.node.Node;
import ast.node.dec.FunNode;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class ArrowTypeNode implements TypeNode {

  private List<TypeNode> parlist;
  private TypeNode ret;
  
  public ArrowTypeNode (List<TypeNode> p, TypeNode r) {
    parlist=p;
    ret=r;
  }
  
  public TypeNode dereference() throws SimplanPlusException {//TODO qualcosa di più elegante?
      throw new SimplanPlusException("Attempt to dereference a bool");
  }
    
  public String toPrint(String s) throws SimplanPlusException { //
	String parlstr="";
    for (Node par:parlist)
      parlstr+=par.toPrint(s+"  ");
	return s+"ArrowType\n" + parlstr + ret.toPrint(s+"  ->") ; 
  }
  
  public TypeNode getRet () { //
    return ret;
  }
  
  public List<TypeNode> getParList () { //
    return parlist;
  }

  @Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		// TODO Auto-generated method stub
		return new ArrayList<SemanticError>();
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
  public String codeGeneration(Label labelManager) {
		return "";
  }

}  