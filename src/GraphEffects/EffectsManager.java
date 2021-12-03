package GraphEffects;


import ast.node.ArgNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.dec.VarNode;
import ast.node.types.ArrowTypeNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.TypeNode;
import semantic.SimplanPlusException;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class EffectsManager {
    private int nl = -1;
    private Graph g;

    public EffectsManager(int nl) {
        this.nl = nl;
        this.g = new Graph();
    }

    public EffectsManager(int nl, Graph g) {
        this.nl = nl;
        this.g = g;
    }

    public void declare(String name, TypeNode type, int nl){
        g.add(new GNode(name, type,  nl, Effect.INITIALIZED));
    }

    public void read(String name, int dereference_level){
        GNode n = g.find(name);
        //System.out.println("reading: "+ name+" "+dereference_level);
        SetWithIdentity<GNode> reading_span = n.span(dereference_level);
        //System.out.println(reading_span);

        for (GNode m: reading_span){
            m.setEffect(Effect.read(m.getEffect()));
        }
        afterOp("read of "+name);
    }


    public void write(String name, int dereference_level){
        read(name, dereference_level-1);
        writeCircle(name, dereference_level);
    }

    public void writeCircle(String name, int dereference_level){
        GNode n = g.find(name);
        SetWithIdentity<GNode> writing_circle = n.circle(dereference_level);

        if (writing_circle.isEmpty()){
            System.out.println("Error in write operation");
        }

        for (GNode m: writing_circle){
            m.setEffect(Effect.write(m.getEffect()));
        }
    }

    public void assign(String namel, String namer, int derefl, int derefr){
        GNode nl=g.find(namel);
        GNode nr=g.find(namer);

        read(namel, derefl-1);
        writeCircle(namel, derefl);

        read(namer, derefr);

        for (GNode n: nl.circle(derefl)){
            nl.setEffect(Effect.write(nl.getEffect()));
            n.setNext(nr.circle(derefr+1));
        }
        afterOp("assignment");

    }

    public void assign_new(String namel, int derefl){
        GNode nl=g.find(namel);
        GNode nr= new GNode("", ((PointerTypeNode) nl.getType()).dereference(), -3, Effect.INITIALIZED);

        read(namel, derefl-1);
        writeCircle(namel, derefl);

        SetWithIdentity<GNode> new_next = new SetWithIdentity<>();

        new_next.add(nr);

        for (GNode n: nl.circle(derefl)){
            n.setNext(new_next);
        }
        g.add(nr);
        afterOp("new");
    }

    public void delete(String id, int derefl){
        GNode nl=g.find(id);

        read(id, derefl-1);
        nl.setEffect(Effect.write(nl.getEffect()));

        SetWithIdentity<GNode> new_next = new SetWithIdentity<>();

        for (GNode n: nl.circle(derefl+1)){
            n.setEffect(Effect.delete(n.getEffect()));
        }
        afterOp("delete");
    }


    public void popNl(int nl){
        ArrayList<GNode> tbr= new ArrayList<>();
        for(GNode n: g.getNodes()){
            if (n.getNesting_level()>=nl)
                tbr.add(n);
        }
        for (GNode n: tbr){
            g.rem(n);
        }
    }

    public boolean checkErr(){
        for (GNode n: g.getNodes()){
            if (Effect.ERROR.equals(n.getEffect()))
                return true;
        }
        return false;
    }

    public void afterOp(String op_name){
        if(checkErr())
            System.out.println("Error in operation "+op_name);
        g.collect();
    }

    @Override
    public String toString() {
        return "EffectsManager{" +
                "nl=" + nl +
                ", g=" + g +
                '}';
    }

    public void incNl(){
        nl++;
    }
    public void decNl(){
        nl--;
    }
    public Graph getG(){
        return g;
    }
    public int getNl() {
        return nl;
    }
    public void setG(Graph new_graph) {
        g=new_graph;
    }

    public Graph prepareFunction(FunNode fun, int nesting_level) {
        Graph res = new Graph();
        GNode tmp;
        res.add(new GNode("+"+fun.getId()+"+return", fun.type, nesting_level, Effect.UNTOUCHED));
        for (Node par: fun.getPars()){
            tmp=new GNode(((ArgNode) par).getIdNode().getID(), ((ArgNode) par).getType(), nesting_level, Effect.UNTOUCHED);
            res.add(tmp);
            for (int i =0; i< ((ArgNode) par).getDereferenceLevel(); i++)
                res.appendUntouched(tmp);
        }
        return res;
    }
}
