package Interpreter.memory;

import semantic.SimplanPlusException;

public class Memory {
    private Cell[] mem;

    public Memory(int size){
        mem = new Cell[size];
        for (int i=0; i< size; i++) {
            mem[i]= new Cell();
        }
    }

    public int read(int n) throws SimplanPlusException {
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

    public void cleanMemory(int start, int end) {
        for (int indexStack = start;indexStack < end; indexStack++){
            mem[indexStack] = new Cell();
        }
    }
}
