package ast.node.statements;

import java.util.ArrayList;

import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import ast.node.types.TypeUtils;
import ast.node.types.VoidTypeNode;
import util.Environment;
import util.Label;
import util.SemanticError;

public class RetNode implements Node {

  private Node val;
  private TypeNode etype;// expected type
  private String endFunction;

    public RetNode (Node v, TypeNode e) {
    val=v;
    etype=e;
  }
  
  
  public TypeNode getEtype() {
	  return etype;
  }

  public String toPrint(String s) {
    return s+"Return\n" + (val!= null ? val.toPrint(s+"  ") : "");
  }

  @Override
 	public ArrayList<SemanticError> checkSemantics(Environment env) {
	  ArrayList<SemanticError> res = new ArrayList<SemanticError> ();

	  if (! (etype instanceof TypeNode))
          res.add(new SemanticError("Return expression out of a function"));
      if(val != null){
          res.addAll(val.checkSemantics(env));
      }

	  return res;
 	}

  
  public TypeNode typeCheck() {
        if(val == null){
            return new VoidTypeNode();
        }
        else if (TypeUtils.isSubtype(val.typeCheck(), etype))
            return etype;
        else{
            System.out.println("Wrong return type for function");
            System.exit(0);
            return null;
        }
  }  
  
  public RetEffType retTypeCheck(FunNode funNode) {
        endFunction = funNode.get_end_fun_label();
	    return new RetEffType(RetEffType.RetT.PRES);
  }

    
  public String codeGeneration(Label labelManager) {
        StringBuilder cgen = new StringBuilder();
        if( val != null){
            cgen.append(val.codeGeneration(labelManager)).append("\n");
        }
        cgen.append("subi $sp $fp 1 //Restore stackpointer as before block creation in return \n");
        cgen.append("lw $fp 0($fp) //Load old $fp pushed \n");

        cgen.append("b ").append(endFunction).append("\n");
		return cgen.toString();
  }
    
}  