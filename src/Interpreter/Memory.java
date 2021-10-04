package Interpreter;

public class Memory {
    private Cell[] mem;

    Memory (int size){
        mem = new Cell[size];
        for (int i=0; i< size; i++) {
            mem[i]= new Cell();
        }
    }

    public int read(int n){
        return mem[n].read();
    }

    public int write(int add, int val){
        return mem[add].write(val);
    }

    public void free(int add){
        mem[add].free();
    }

    public int allocate(){
        for (int i=0; i< mem.length; i++){
            if (!mem[i].isPointed()){
                mem[i].allocate();
                return i;
            }
        }
        return -1;
    }
}
