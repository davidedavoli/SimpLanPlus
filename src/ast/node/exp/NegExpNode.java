package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.types.IntTypeNode;
import ast.node.types.TypeUtils;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class NegExpNode extends ExpNode {

  	private final ExpNode exp;
  
  	public NegExpNode (ExpNode e) {
    exp=e;
  }

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) {
		return new ArrayList<>(exp.checkSemantics(env));
	}

	public TypeNode typeCheck() {
		if (! TypeUtils.isSubtype(exp.typeCheck(),new IntTypeNode())) {
			System.err.println("Trying to do negative on a non int");
			System.exit(0);
		}
		return new IntTypeNode();
	}
	public HasReturn retTypeCheck() {
		return new HasReturn(HasReturn.hasReturnType.ABS);
	}

	@Override
	public ArrayList<EffectError> checkEffects (Environment env) {
		ArrayList<EffectError> errors = new ArrayList<>();
		errors.addAll(exp.checkEffects(env));
		errors.addAll(checkExpStatus(env));
		return errors;
	}

	@Override
	public String codeGeneration(Label labelManager) {
		StringBuilder codeGenerated = new StringBuilder();
		String loaded_exp = exp.codeGeneration(labelManager);
		codeGenerated.append(loaded_exp).append("\n");
		codeGenerated.append("multi $a0 $a0 -1 //do negate\n");
		return codeGenerated.toString();
	}


	@Override
	public List<Dereferences> variables() {
		return exp.variables();
	}

	public String toPrint(String s) {
		return "neg " + exp.toPrint(s);
	}

}  