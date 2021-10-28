package ast.node.exp;

import ast.Dereferenceable;
import ast.Label;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.statements.CallNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
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
    public RetEffType retTypeCheck(FunNode funNode) {
        return inner.retTypeCheck(funNode);
    }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return inner.checkEffects(env);
    }

    @Override
    public List<Dereferenceable> variables() {
        List<Dereferenceable> l = new ArrayList<>();
        for (ExpNode par: inner.getParlist())
            l.addAll(par.variables());
        return l;
    }
}
