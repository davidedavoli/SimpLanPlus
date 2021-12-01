package ast.node.types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ast.node.Node;
import effect.EffectError;
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
  
  public TypeNode dereference() {
    System.err.println("Attempt to dereference a function");
    System.exit(0);
    return null;
      //throw new SimplanPlusException("Attempt to dereference a bool");
  }

  public String toString(){
    String s="";
    s= parlist.stream()
            .map(Object::toString)
            .collect(Collectors.joining("x"));
    s+="->";
    s+= ret.getClass().getName();
    return s;
  }
    
  public String toPrint(String s) { //
	StringBuilder parlstr= new StringBuilder();
    for (Node par:parlist)
      parlstr.append(par.toPrint(s + "  "));
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
  public String codeGeneration(Label labelManager) {
		return "";
  }

}  