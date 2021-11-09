package ast;

import ast.node.LhsNode;
import ast.node.exp.IdExpNode;
import semantic.Effect;

public interface Dereferenceable {

    int getDereferenceLevel();
    String getID();
    STentry getEntry();

    Effect getIdStatus(int j);
}