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
            System.err.println("\nValue is not written in memory.\nExit program...");
            System.exit(1);
        }
        return val;
    }

    public boolean isPointed(){
        return pointed;
    }
}
