package ast.node.types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import GraphEffects.EffectsManager;
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
  
  public TypeNode dereference() throws SimplanPlusException {//TODO qualcosa di più elegante?
      throw new SimplanPlusException("Attempt to dereference a bool");
  }


  public String toString(){
    String s="";
    s=String.join("x", parlist.stream()
            .map(item -> item.toString())
            .collect(Collectors.toList()));
    s+="->";
    s+= ret.getClass().getName();
    return s;
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
  @Override
  public void checkGraphEffects(EffectsManager m) {
    return;
  }


}  