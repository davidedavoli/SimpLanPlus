package ast.node.types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ast.node.Node;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class ArrowTypeNode implements TypeNode {

  private final List<TypeNode> parameterList;
  private final TypeNode ret;
  
  public ArrowTypeNode (List<TypeNode> p, TypeNode r) {
    parameterList =p;
    ret=r;
  }
  
  public TypeNode dereference() {
    System.err.println("Attempt to dereference a function");
    System.exit(0);
    return null;
  }

  public String toString(){
    String s;
    s= parameterList.stream()
            .map(Object::toString)
            .collect(Collectors.joining("x"));
    s+="->";
    s+= ret.getClass().getName();
    return s;
  }
    
  public String toPrint(String s) { //
	StringBuilder parameterListString= new StringBuilder();
    for (Node par: parameterList)
      parameterListString.append(par.toPrint(s + "  "));
	return s+"ArrowType\n" + parameterListString + ret.toPrint(s+"  ->") ;
  }
  
  public TypeNode getRet () { //
    return ret;
  }
  
  public List<TypeNode> getParList () { //
    return parameterList;
  }

  @Override
  public ArrayList<SemanticError> checkSemantics(Environment env) {
    return new ArrayList<>();
  }
  
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

  public String codeGeneration(Label labelManager) {
		return "";
  }

}  