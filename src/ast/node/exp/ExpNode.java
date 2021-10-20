package ast.node.exp;

import ast.Label;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.types.RetEffType;
import ast.node.types.TypeNode;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

import java.util.ArrayList;

public class ExpNode implements Node {
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
        return null;
    }

    public ArrayList<SemanticError> checkExpStatus(Environment env) {
        return null;
    }
}
