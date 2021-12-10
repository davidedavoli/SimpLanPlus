package ast.node.exp;

import ast.Dereferences;
import ast.Label;
import ast.node.statements.CallNode;
import ast.node.types.HasReturn;
import ast.node.types.TypeNode;
import effect.EffectError;
import semantic.Environment;
import semantic.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class CallExpNode extends ExpNode {
    CallNode inner;
    public CallExpNode(CallNode inner) {
        this.inner=inner;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return inner.checkSemantics(env);
    }

    @Override
    public TypeNode typeCheck() {
        return inner.typeCheck();
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
    public String codeGeneration(Label labelManager) {
        return inner.codeGeneration(labelManager);
    }


    @Override
    public List<Dereferences> variables() {
        List<Dereferences> l = new ArrayList<>();
        for (ExpNode par: inner.getParameterList())
            l.addAll(par.variables());
        return l;
    }

    @Override
    public String toPrint(String indent) {
        return inner.toPrint(indent);
    }
}
