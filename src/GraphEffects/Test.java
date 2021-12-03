package GraphEffects;

import ast.node.types.IntTypeNode;
import effect.Effect;

import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {
        EffectsManager m=new EffectsManager(0);
        m.declare("x",new IntTypeNode(), 0);
        m.declare("y", new IntTypeNode(), 0);
        m.write("y", 0);
//        m.assign_new("x", 0);
//        m.assign_new("y", 0);
        System.out.println(m);
        m.assign("x", "y", 0, 0);
        System.out.println(m.getG().copy());
        System.out.println(m);
        /*
        m.delete("x", 0);
        System.out.println(m);

        m.assign("x", "y", 0,0);
        System.out.println(m);
        m.read("x", 1);
        System.out.println(m);
        m.assign_new("x", 0);
//        System.out.println(m);
        */
    }
}
