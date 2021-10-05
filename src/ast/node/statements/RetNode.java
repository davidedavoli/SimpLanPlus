package ast.node.statements;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

public class RetNode implements Node {

  private Node val;
  private TypeNode etype;// expected type
  
  public RetNode (Node v, TypeNode e) {
    val=v;
    etype=e;
  }
  
  
  public TypeNode getEtype() {
	  return etype;
  }

  public String toPrint(String s) {
    return s+"Return\n" + val.toPrint(s+"  ") ;
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  ArrayList<SemanticError> res = new ArrayList<SemanticError> ();

	  if (! (etype instanceof TypeNode))
          res.add(new SemanticError("Return expression out of a function"));

	  res.addAll(val.checkSemantics(env));
	  return res;
 	}

  
  public TypeNode typeCheck() {
    if (TypeUtils.isSubtype(val.typeCheck(), etype))
    	return etype;
    else{
        System.out.println("Wrong return type for function");
        System.exit(0);
    	return null;
    }
  }  
  
  public RetEffType retTypeCheck() {
	  return new RetEffType(RetEffType.RetT.PRES);
  }
    
  public String codeGeneration(Label labelManager) {
		return val.codeGeneration(labelManager)+"";//TODO Codice
  }
    
}  