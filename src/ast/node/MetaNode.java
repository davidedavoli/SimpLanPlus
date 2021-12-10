package ast.node;


import java.util.ArrayList;

public abstract class MetaNode implements Node {
    private MetaNode parent;

    public void setParent(MetaNode p) {
        this.parent=p;
    }

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
