package ast.node.exp;

import java.util.ArrayList;
import java.util.List;

import ast.Dereferences;
import ast.node.types.BoolTypeNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;

public class NotExpNode extends ExpNode {

  	private final ExpNode exp;
  
  	public NotExpNode (ExpNode e) {
    exp=e;
  }

	@Override
	public ArrayList<SemanticError> checkSemantics(Environment env) { return new ArrayList<>(exp.checkSemantics(env)); }

	public TypeNode typeCheck() {
		TypeNode expType = exp.typeCheck();

		if (! (expType instanceof BoolTypeNode)) {
			System.err.println("Trying to do negate (!) of a non bool");
			System.exit(0);
		}
		//throw new SimplanPlusException("Exp not bool, throw exception");

		return new BoolTypeNode();

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

	public String codeGeneration(Label labelManager) {
		StringBuilder codeGenerated = new StringBuilder();
		String loaded_exp = exp.codeGeneration(labelManager);
		codeGenerated.append(loaded_exp).append("\n");
		codeGenerated.append("not $a0 $a0\n");

		return codeGenerated.toString();

	}

	@Override
	public List<Dereferences> variables() {
		return exp.variables();
	}

	public String toPrint(String s) {
		return "not " + exp.toPrint(s);
	}
}  