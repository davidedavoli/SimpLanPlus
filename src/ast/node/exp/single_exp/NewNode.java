package ast.node.exp.single_exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferenceable;
import ast.node.exp.ExpNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import semantic.SimplanPlusException;

public class NewNode extends ExpNode {
	
	  private TypeNode type;
	  
	  public NewNode (TypeNode t) {
		    type=t;
		  }
	  
	  @Override
	  public ArrayList<SemanticError> checkSemantics(Environment env) {
		  //create result list
		  ArrayList<SemanticError> res = new ArrayList<SemanticError>();
		  
	  	  if (type == null)
	  		  res.add(new SemanticError("new operator in compound expression"));
	      return res;
	  }
	  
	  public String toPrint(String s) {
		return s+"New:\n" + type.toPrint(s+"   ") +"\n";
	  }
	  
	  //valore di ritorno non utilizzato
	  public TypeNode typeCheck() {
	    return new PointerTypeNode(type);
	  }
	  
	  public HasReturn retTypeCheck() {
		  return new HasReturn(HasReturn.hasReturnType.ABS);
	  }

	@Override
	public ArrayList<EffectError> checkEffects (Environment env) {
		return new ArrayList<>();
	}

	@Override
	public List<Dereferenceable> variables() {
		return new ArrayList<Dereferenceable>();
	}

	public String codeGeneration(Label labelManager){
		  StringBuilder cgen = new StringBuilder();
		  cgen.append("new $a0").append("// put new address in a0\n");
		  return cgen.toString();
	  }  
}  