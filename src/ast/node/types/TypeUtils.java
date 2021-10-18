package ast.node.types;

import ast.node.Node;
import util.SimplanPlusException;

public class TypeUtils {
    public static boolean isSubtype (TypeNode a, TypeNode b) throws SimplanPlusException {
        if (a instanceof PointerTypeNode && b instanceof PointerTypeNode){
            return isSubtype(a.dereference(), b.dereference());
        }
        return a.getClass().equals(b.getClass());
    }
}
