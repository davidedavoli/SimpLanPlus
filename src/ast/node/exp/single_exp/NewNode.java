package ast.node.exp.single_exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.exp.ExpNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class NewNode extends ExpNode {

	private final TypeNode type;
	  
	public NewNode (TypeNode t) {
		    type=t;
		  }

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		ArrayList<SemanticError> res = new ArrayList<>();
		if (type == null)
	  		res.add(new SemanticError("new operator in compound expression", ""));
		return res;
	}

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
	public List<Dereferences> variables() {
		return new ArrayList<>();
	}

	public String codeGeneration(Label labelManager) {
		return "new $a0" + "// put new address in a0\n";
	  }

	public String toPrint(String s) {
		return s+"New:\n" + type.toPrint(s+"   ") +"\n";
	}
}  