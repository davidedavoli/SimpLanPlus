package ast.node;


import ast.Dereferences;

import java.util.ArrayList;
import java.util.List;

public abstract class MetaNode implements Node {
    private MetaNode parent;

    public void setParent(MetaNode p) {
        this.parent=p;
    }

    public abstract List<Dereferences> variables();

    public ArrayList<Node> getAncestorsInstanceOf(Class<?> c) {
        if(this.parent== null){
            return new ArrayList<>();
        }
        if (c.isInstance(this.parent)){
            ArrayList<Node> res = parent.getAncestorsInstanceOf(c);
            res.add(0, parent);
            return res;
        }
        return parent.getAncestorsInstanceOf(c);
    }
}
