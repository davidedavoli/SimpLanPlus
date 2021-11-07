package ast;

import semantic.Effect;

public interface Dereferenceable {

    int getDerefLevel();
    String getID();

}