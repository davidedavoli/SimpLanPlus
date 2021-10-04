package Interpreter.ast;

public class Instruction {
    private int code;
    private String arg1;
    private String arg2;
    private String arg3;

    public Instruction(int c){
        code=c;
        arg1 = arg2 = arg3 = null;
    }
    public Instruction(int c, String a1){
        arg1 = arg2 = arg3 = null;
        code = c;
        arg1 = a1;
    }

    public Instruction(int c, String a1, String a2){
        arg1 = arg2 = arg3 = null;
        code = c;
        arg1 = a1;
        arg2 = a2;
    }

    public Instruction(int c, String a1, String a2, String a3){
        arg1 = arg2 = arg3 = null;
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

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
