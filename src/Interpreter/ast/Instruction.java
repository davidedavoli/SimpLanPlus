package Interpreter.ast;

public class Instruction {
    private int code;
    private final String arg1;
    private final String arg2;
    private final String arg3;

    public Instruction(int c){
        code=c;
        arg1 = arg2 = arg3 = null;
    }
    public Instruction(int c, String a1){
        code = c;
        arg1 = a1;
        arg2 = arg3 = null;
    }

    public Instruction(int c, String a1, String a2){
        code = c;
        arg1 = a1;
        arg2 = a2;
        arg3 = null;
    }

    public Instruction(int c, String a1, String a2, String a3){
        code = c;
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
    }

    public int getCode() {
        return code;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }
    public String getArg3(){
        return arg3;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
