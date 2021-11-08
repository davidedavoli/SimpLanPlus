package ast;

import ast.node.LhsNode;
import ast.node.exp.IdExpNode;
import semantic.Effect;

public interface Dereferenceable {

    int getDerefLevel();
    String getID();
    STentry getEntry();

}