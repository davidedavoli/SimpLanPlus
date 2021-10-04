package ast.node.types;

import ast.node.Node;

public class TypeUtils {
    public static boolean isSubtype (Node a, Node b) {
        return a.getClass().equals(b.getClass());
    }
}
