package Interpreter.memory;

import semantic.SimplanPlusException;

public class Cell {
    private Integer val;
    private boolean pointed;

    Cell(){
        pointed=false;
        val = null;
    }

    public Integer write(int v){
        val = v;
        return val;
    }

    public void free(){
        pointed=false;
    }

    public void allocate(){
        pointed=true;
    }

    public Integer read() throws SimplanPlusException {
        if(val == null){
            System.err.println("Value is not written in memory");
            System.exit(1);
        }
        return val;
    }

    public boolean isPointed(){
        return pointed;
    }
}
