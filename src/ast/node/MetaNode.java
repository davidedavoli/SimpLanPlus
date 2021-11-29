package ast.node;


import java.util.ArrayList;
import java.util.List;

public abstract class MetaNode implements Node {
    private MetaNode parent;
    private final List<MetaNode> children = new ArrayList<>();

    public MetaNode getParent() {
        return parent;
    }
    public void setParent(MetaNode p) {
        this.parent=p;
        p.addChild(this);
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

    public void addChild(MetaNode child){
        children.add(child);
    }
    public List<MetaNode> getChildren(){
        return this.children;
    }

    public List<MetaNode> getGrandChildren(){
        List<MetaNode> res=new ArrayList<>();
        for (MetaNode n: children){
            res.addAll(n.getGrandChildren());
            res.add(n);
        }
        return res;
    }




}
