package types;
import java.util.ArrayList;

import ast.Node;
import util.Environment;
import util.SemanticError;

public class ArrowTypeNode implements TypeNode {

  private ArrayList<TypeNode> parlist; 
  private TypeNode ret;
  
  public ArrowTypeNode (ArrayList<TypeNode> p, TypeNode r) {
    parlist=p;
    ret=r;
  }
  
  public TypeNode dereference() {//TODO qualcosa di più elegante?
      System.out.println("Attempt to dereference a bool");
      System.exit(0);
      return null;
  }
    
  public String toPrint(String s) { //
	String parlstr="";
    for (Node par:parlist)
      parlstr+=par.toPrint(s+"  ");
	return s+"ArrowType\n" + parlstr + ret.toPrint(s+"  ->") ; 
  }
  
  public TypeNode getRet () { //
    return ret;
  }
  
  public ArrayList<TypeNode> getParList () { //
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
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.ABS);
  }

  //non utilizzato
  public String codeGeneration() {
		return "";
  }

}  