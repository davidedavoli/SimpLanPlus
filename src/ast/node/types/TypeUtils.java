package ast.node.types;

import semantic.SimplanPlusException;

public class TypeUtils {
    public static boolean isSubtype (TypeNode a, TypeNode b) throws SimplanPlusException {
        if (a instanceof PointerTypeNode && b instanceof PointerTypeNode){
            return isSubtype(a.dereference(), b.dereference());
        }
        return a.getClass().equals(b.getClass());
    }
}
