package GraphEffects;

import ast.node.types.PointerTypeNode;
import effect.Effect;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static GraphEffects.GNode.nv_name;

public class Graph {
    private SetWithIdentity<GNode> nodes;

    public Graph() {
        this.nodes = new SetWithIdentity<>();
    }
    public Graph(SetWithIdentity<GNode> nodes) {
        this.nodes = nodes;
    }

    public Graph copy() {
        Graph h = new Graph();
        ArrayList<GNode> original = new ArrayList<>();
        ArrayList<GNode> cp = new ArrayList<>();

        for (GNode n : nodes) {
            original.add(n);
            cp.add(new GNode(n.getId(), n.getName(), n.getType(), n.getNesting_level(), n.getEffect()));
        }

        for (GNode n : nodes) {
            for (GNode m : nodes)
                if (m.reaches1(n)) {
                    cp.get(original.indexOf(m)).addNext(cp.get(original.indexOf(n)));
                }
        }

        for (GNode c : cp) {
            h.add(c);
        }

        return h;
    }


    public SetWithIdentity<GNode> getNodes() {
        return nodes;
    }

    public GNode get(String id, int nl) {
        for (GNode n : nodes) {
            if (n.getName().equals(id) && n.getNesting_level() == nl) {
                return n;
            }
        }
        return null;
    }

    public void add(GNode n) {
        nodes.add(n);
    }

    public GNode find(String id, int nl) {
        for (GNode n : nodes) {
            if (n.getNesting_level() == nl && n.getName() == id) {
                return n;
            }
        }
        return null;
    }

    public GNode find(String id) {
        int min = -2;
        GNode toret = null;

        List<GNode> vars = new ArrayList<>();
        for (GNode n : nodes) {
            if (n.getName().equals(id)) {
                vars.add(n);
            }
        }

        for (GNode v : vars) {
            if (v.getNesting_level() > min) {
                toret = v;
                min = v.getNesting_level();
            }
        }
        return toret;
    }


    public void rem(GNode n) {
        for (GNode m : nodes) {
            if (m.reaches1(n)) {
                m.remNext(n);
            }
        }
        nodes.remove(n);
    }

    public ArrayList<GNode> checkErrors() {
        ArrayList<GNode> res = new ArrayList<>();

        for (GNode n : nodes) {
            if (n.getEffect().equals(Effect.ERROR)) {
                res.add(n);
            }
        }
        return res;
    }

    public SetWithIdentity<GNode> variables() {
        SetWithIdentity<GNode> res = new SetWithIdentity<>();
        nodes.stream().filter((n) -> !n.getName().equals(nv_name)).collect(Collectors.toSet());
        res.addAll(nodes.stream().filter((n) -> !n.getName().equals(nv_name)).collect(Collectors.toSet()));
        return res;
    }

    public void collect() {
        SetWithIdentity<GNode> reachable = new SetWithIdentity<>();
        for (GNode n : variables()) {
            reachable.addAll(n.reachableSubGraph());
        }
        this.nodes.retainAll(reachable);
        for (GNode n : nodes) {
            n.retainNext(reachable);
        }
    }

    public void join(GNode n, GNode m) {
        n.addNext(m);
    }

    public void unJoin(GNode n, GNode m) {
        n.remNext(m);
    }

    public static Graph sup(Graph a, Graph b) {
        a=a.copy();
        b=b.copy();
        Graph res = new Graph();
        for (GNode na : a.getNodes())
            res.add(na);
        for (GNode nb : b.getNodes()) {
            if (res.getNodes().contains(nb) && res.getNodes().get(nb).getEffect().le(nb.getEffect())) {
                res.getNodes().get(nb).setEffect(nb.getEffect());
            }
            if (!res.getNodes().contains(nb)) {
                res.add(nb);
            }
        }
        SetWithIdentity<GNode> next_a = new SetWithIdentity<>();
        SetWithIdentity<GNode> next_b = new SetWithIdentity<>();
        for (GNode n : res.nodes) {
            if (a.nodes.contains(n)) {
                next_a = a.getNodes().get(n).getNext();
            }
            if (b.nodes.contains(n)) {
                next_b = b.getNodes().get(n).getNext();
            }
            next_a.addAll(next_b);
            n.setNext(next_a);
            next_a = new SetWithIdentity<>();
            next_b = new SetWithIdentity<>();
        }
        return res;
    }

    public Graph partition(Function<GNode, Boolean> condition) {
        Graph res = new Graph();
        SetWithIdentity<GNode> starting_set = new SetWithIdentity<>();
        for (GNode n : nodes) {
            if (condition.apply(n)) {
                starting_set.add(n);
            }
        }

        for (GNode n : starting_set) {
            for (GNode m : n.reachableSubGraph()) {
                res.add(m);
            }
        }

        return res;
    }

    public void map(Function<GNode, GNode> f) {
        for (GNode n : nodes) {
            f.apply(n);
        }
    }

    public void appendUntouched(GNode n) {
        if (nodes.get(n) == null)
            return;
        GNode tmp = new GNode("", ((PointerTypeNode) (n.getType())).dereference(), -3, GraphEffects.Effect.UNTOUCHED);
        nodes.add(tmp);
        nodes.get(n).getNext().add(tmp);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                '}';
    }

    public Map<String, Set<String>> traces(){
        Map<String, Set<String>> res= new HashMap<String, Set<String>>();

        for (GNode n: variables()){
            res.put(n.getName(), n.traces());
        }
        return res;
    }

    public static boolean sameTraces(Graph a, Graph b){
        Map<String, Set<String>> t1 = a.traces();
        Map<String, Set<String>> t2 = b.traces();

        if (!t1.keySet().containsAll(t2.keySet()) || !t2.keySet().containsAll(t1.keySet())){
            return false;
        }
        for (String ka: t1.keySet()){
            if (!t1.get(ka).containsAll(t2.get(ka)) || !t2.get(ka).containsAll(t1.get(ka))){
                return false;
            }
        }

        return true;
    }

}
