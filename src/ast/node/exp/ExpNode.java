package ast.node.exp;

import ast.Dereferenceable;
import ast.Label;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Effect;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpNode implements Node {
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
    public RetEffType retTypeCheck(FunNode funNode) {
        return null;
    }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return new ArrayList<>();
    }


    public abstract List<Dereferenceable> variables();

    protected ArrayList<SemanticError> checkExpStatus(Environment env) {
        ArrayList<SemanticError> errors = new ArrayList<>();

        variables().forEach(var -> errors.addAll(env.checkStmStatus(var, Effect::sequenceEffect, Effect.READWRITE)));

        return errors;
    }
}
