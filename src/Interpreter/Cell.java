package Interpreter;

public class Cell {
    private int val;
    private boolean pointed;

    Cell(){
        pointed=false;
    }

    public int write(int v){
        return val=v;
    }

    public void free(){
        pointed=false;
    }

    public void allocate(){
        pointed=true;
    }

    public int read(){
        return val;
    }

    public boolean isPointed(){
        return pointed;
    }
}
