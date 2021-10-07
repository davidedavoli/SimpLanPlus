package ast.node.types;

import semanticAnalysis.Environment;
import semanticAnalysis.Label;
import semanticAnalysis.SemanticError;

import java.util.ArrayList;
import java.util.List;

public abstract class FunTypeNode implements TypeNode {

    private List<TypeNode> params;
    private TypeNode returned;

    public FunTypeNode(List<TypeNode> params, TypeNode returned) {
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
    public RetEffType retTypeCheck() {
        return new RetEffType(RetEffType.RetT.ABS);
    }

    @Override
    public ArrayList<SemanticError> checkEffects(Environment env) {
        return new ArrayList<>();
    }

    @Override
    public String toPrint(String indent) {
        return indent +
                params.stream().map(TypeNode::toString).reduce("",
                        (arg1, arg2) -> (arg1.isEmpty() ? "" : (arg1 + " X ")) + arg2)
                + " -> " + returned;
    }

    @Override
    public TypeNode typeCheck() {
        return null;
    }

    @Override
    public String codeGeneration(Label labelManager) {
        return "";
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(Environment env) {
        return null;
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
}
