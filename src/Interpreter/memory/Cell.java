package Interpreter.memory;

public class Cell {
    private Integer val;
    private boolean pointed;

    Cell(){
        pointed=false;
        val = null;
    }

    public void write(int v){
        val = v;
    }

    public void free(){
        pointed=false;
        val=null;
    }

    public void allocate(){
        pointed=true;
    }

    public Integer read() {
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
