package ast.node.exp;

import GraphEffects.EffectsManager;
import ast.Dereferenceable;
import ast.Label;
import ast.node.MetaNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.Effect;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpNode extends MetaNode {
    @Override
    public String toPrint(String indent) throws SimplanPlusException {
        return null;
    }

    @Override
    public TypeNode typeCheck() throws SimplanPlusException {
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        return null;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
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


    public abstract List<Dereferenceable> variables();

    protected ArrayList<EffectError> checkExpStatus(Environment env) {
        ArrayList<EffectError> errors = new ArrayList<>();

        variables().forEach(var -> errors.addAll(env.checkStmStatus(var, Effect::sequenceEffect, Effect.READWRITE)));

        return errors;
    }

    public abstract void readGraphEffect(EffectsManager m);

    public abstract void checkGraphEffects(EffectsManager m);
}
