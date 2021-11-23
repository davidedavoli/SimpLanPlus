package ast.node.types;



import ast.Label;
import ast.node.Node;
import ast.node.dec.FunNode;
import semantic.Environment;
import semantic.SemanticError;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a function type ((args) -> type) token in the AST.
 */
public class FunTypeNode implements TypeNode {

    final private List<TypeNode> params;
    final private TypeNode returned;

    public FunTypeNode(final List<TypeNode> params, final TypeNode returned) {
        this.params = params;
        this.returned = returned;
    }

    public List<TypeNode> getParams() {
        return params;
    }

    public TypeNode getReturned() {
        return returned;
    }

    @Override
    public String toPrint(String indent) {
        return indent +
                params.stream().map(TypeNode::toString).reduce("",
                        (arg1, arg2) -> (arg1.isEmpty() ? "" : (arg1 + " X ")) + arg2)
                + " -> " + returned;
    }

    @Override
    public TypeNode typeCheck(){
        return null; // Nothing to return
    }

    @Override
    public TypeNode dereference() throws SimplanPlusException {
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) {
        return null;
    }

    @Override
    public RetEffType retTypeCheck(FunNode funNode) {
        return null;
    }
    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        FunTypeNode funTypeNode = (FunTypeNode) obj;

        if (!returned.equals(funTypeNode.returned)) {
            return false;
        }

        if (!params.equals(funTypeNode.params)) {
            return false;
        }

        return true;
    }

    public List<TypeNode> getArgumentsType() {
        return params;
    }
}
