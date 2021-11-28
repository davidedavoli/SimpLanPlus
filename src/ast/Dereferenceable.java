package ast;

import effect.Effect;

public interface Dereferenceable {

    int getDereferenceLevel();
    String getID();
    STentry getEntry();

    Effect getIdStatus(int j);

    Boolean isPointer();
}