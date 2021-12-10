package Interpreter.memory;

public class Memory {
    private final Cell[] mem;

    public Memory(int size){
        mem = new Cell[size];
        for (int i=0; i< size; i++) {
            mem[i]= new Cell();
        }
    }

    public int read(int n){
        return mem[n].read();
    }

    public void write(int add, int val){
        mem[add].write(val);
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
