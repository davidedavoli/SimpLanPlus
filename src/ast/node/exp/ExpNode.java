package ast.node.exp;

import ast.Dereferences;
import ast.Label;
import ast.node.MetaNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpNode extends MetaNode {

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return null;
    }

    @Override
    public TypeNode typeCheck() {
        return null;
    }
    @Override
    public HasReturn retTypeCheck() {
        return null;
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        return new ArrayList<>();
    }

    @Override
    public String codeGeneration(Label labelManager) {
        return null;
    }


    public abstract List<Dereferences> variables();

    protected ArrayList<EffectError> checkExpStatus(Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();
        variables().forEach(var -> errors.addAll(env.checkStmStatus(var, Effect::sequenceEffect, Effect.READWRITE)));

        return errors;
    }

    @Override
    public String toPrint(String indent) {
        return null;
    }
}
