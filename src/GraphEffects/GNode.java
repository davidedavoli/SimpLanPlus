package GraphEffects;

import ast.node.types.TypeNode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GNode extends WithId {
    private String name;
    private TypeNode type=null;
    private int nesting_level;
    private Effect effect;
    private SetWithIdentity<GNode> next;
    public static final String nv_name = "";

    public GNode(String name, TypeNode type, int nesting_level, Effect effect) {
        super();
        this.name = name;
        this.type = type;
        this.nesting_level = nesting_level;
        this.effect = effect;
        this.next= new SetWithIdentity<>();
    }
    public GNode(int id, String name, TypeNode type, int nesting_level, Effect effect) {
        this.setId(id);
        this.name = name;
        this.type = type;
        this.nesting_level = nesting_level;
        this.effect = effect;
        this.next= new SetWithIdentity<>();
    }

    public GNode() {
        this.name = nv_name;
        this.nesting_level = -3;
        this.effect = Effect.INITIALIZED;
        this.next= new SetWithIdentity<>();
    }

    public TypeNode getType() {
        return type;
    }

    public SetWithIdentity<GNode> span(int r){
        SetWithIdentity<GNode> res= new SetWithIdentity<GNode>();

        if (r==0) {
            res.add(this);
            return res;
        }

        if(r>0) {
            for (GNode m : next) {
                res.addAll(m.span(r - 1));
                res.add(this);
            }
        }
        //System.out.println(res);
        return res;
    }

    public boolean sameVar(GNode m){
        return m.name.equals(name) && m.nesting_level== nesting_level;
    }

    public String getName() {
        return name;
    }

    public SetWithIdentity<GNode> getNext() {
        return next;
    }

    public int getNesting_level() {
        return nesting_level;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNesting_level(int nesting_level) {
        this.nesting_level = nesting_level;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public void addNext(GNode n){
        this.next.add(n);
    }

    public void remNext(GNode m) {
        this.next.remove(m);
    }

    public boolean reaches1(GNode n){
        return next.contains(n);
    }

    public void setNext(SetWithIdentity<GNode> next) {
        this.next = next;
    }

    public SetWithIdentity<GNode> circle (int radius){
        SetWithIdentity<GNode> res=new SetWithIdentity<>();

        if (radius == 0)
            res.add(this);
        else{
            for(GNode n: next){
                res.addAll(n.circle(radius-1));
            }
        }
        //if (radius>0 && res.isEmpty())
        //    System.out.println("ERROR: dereferencing too much");

        return res;
    }

    public SetWithIdentity<GNode> reachableSubGraph() {
        SetWithIdentity<GNode> res= new SetWithIdentity<GNode>();
        res.add(this);
        for (GNode n: next){
            res.addAll(n.reachableSubGraph());
        }
        return res;
    }

    public boolean reachesN(GNode n){
        boolean b= false;
        for (GNode m: next){
            b=b || m.reachesN(n) || n.equals(m);
        }
        return this.reaches1(n);
    }

    @Override
    public String toString() {
        return "GNode{" +
                "name='" + name + '\'' +
                ", id=" + getId()  +
                ", nesting_level=" + nesting_level +
                ", effect=" + effect +
                ", next=" + next +
                '}';
    }

    public void retainNext(SetWithIdentity<GNode> reachable) {
        next.retainAll(reachable);
    }

    public Set<String> traces(){
        return next.stream().map((n)-> n.traces()).map((s)-> effect.toString()+s).collect(Collectors.toSet());
    }
}
