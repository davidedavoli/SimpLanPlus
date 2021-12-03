package ast.node.exp;

import GraphEffects.EffectsManager;
import ast.Dereferenceable;
import ast.Label;
import ast.STentry;
import ast.node.statements.CallNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

public class CallExpNode extends ExpNode {
    CallNode inner;
    public CallExpNode(CallNode inner) {
        this.inner=inner;
    }

    @Override
    public String toPrint(String indent) throws SimplanPlusException {
        return inner.toPrint(indent);
    }
    public STentry innerEntry(){
        return inner.getEntry();
    }
    public String getIdName(){
        return inner.getIdName();
    }
    @Override
    public TypeNode typeCheck() throws SimplanPlusException {
        return inner.typeCheck();
    }

    @Override
    public String codeGeneration(Label labelManager) throws SimplanPlusException {
        return inner.codeGeneration(labelManager);
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) throws SimplanPlusException {
        return inner.checkSemantics(env);
    }

    @Override
    public HasReturn retTypeCheck() {
        return inner.retTypeCheck();
    }

    @Override
    public ArrayList<EffectError> checkEffects (Environment env) {
        return inner.checkEffects(env);
    }

    @Override
    public List<Dereferenceable> variables() {
        List<Dereferenceable> l = new ArrayList<>();
        for (ExpNode par: inner.getParlist())
            l.addAll(par.variables());
        return l;
    }

    @Override
    public void readGraphEffect(EffectsManager m) {
        for (ExpNode e: inner.getParlist())
            e.readGraphEffect(m);
    }

    @Override
    public void checkGraphEffects(EffectsManager m) {
        // TODO
        inner.checkGraphEffects(m);
    }


}
