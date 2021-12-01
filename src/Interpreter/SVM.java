package Interpreter;

import Interpreter.ast.Instruction;
import Interpreter.memory.Memory;
import Interpreter.parser.SVMParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVM {

    public static final int CODE_SIZE = 10000;
    public static final int MEMORY_SIZE = 10000;

    private final Instruction[] code;
    private final Memory memory = new Memory(MEMORY_SIZE);

    private int ip = 0;             // instruction pointer, internal register, no write nor read
    private int sp = MEMORY_SIZE;       // stack pointer
    private int hp = 0;             // heap pointer read-only
    private int fp = MEMORY_SIZE -1;       // frame pointer
    private int ra;
    private int al;
    private int bsp = MEMORY_SIZE;

    private final int[] a = new int[10];
    private final int[] r = new int[10];

    public SVM(Instruction[] code) {
        this.code = code;
    }

    public void cpu() {
        while (true) {
            if (hp + 1 >= sp) {
                System.out.println("\nError: Out of memory");
                return;
            } else {
                Instruction bytecode = code[ip++]; // fetch
                String arg1 = bytecode.getArg1();
                String arg2 = bytecode.getArg2();
                String arg3 = bytecode.getArg3();

                int offset,value;
                int address;
                
                try {
                    switch (bytecode.getCode()) {

                        case SVMParser.PUSH:
                            if (isRegister(arg1))
                                push(regRead(arg1));
                            else
                                push(Integer.parseInt(arg1));
                            break;
                        case SVMParser.POP:
                            if (arg1 != null && isRegister(arg1))
                                regStore(arg1, pop());
                            else
                                pop();
                            break;

                        case SVMParser.ADD:
                            regStore(arg1, regRead(arg2) + regRead(arg3));
                            break;
                        case SVMParser.ADDI:
                            value = Integer.parseInt(arg3);
                            regStore(arg1, regRead(arg2) + value);
                            break;

                        case SVMParser.SUB:
                            regStore(arg1, regRead(arg2) - regRead(arg3));
                            break;
                        case SVMParser.SUBI:
                            value = Integer.parseInt(arg3);
                            regStore(arg1, regRead(arg2) - value);
                            break;

                        case SVMParser.MULT:
                            regStore(arg1, regRead(arg2) * regRead(arg3));
                            break;
                        case SVMParser.MULTI: //Also used for negate (*-1)
                            value = Integer.parseInt(arg3);
                            regStore(arg1, regRead(arg2) * value);
                            break;

                        case SVMParser.DIV:
                            regStore(arg1, regRead(arg2) / regRead(arg3));
                            break;
                        case SVMParser.DIVI:
                            value = Integer.parseInt(arg3);
                            regStore(arg1, regRead(arg2) / value);
                            break;

                        case SVMParser.STOREW:
                            offset = Integer.parseInt(arg2);
                            int addressStoreWord = offset + regRead(arg3);
                            memory.write(addressStoreWord, regRead(arg1));
                            break;
                        case SVMParser.LOAD:
                            value = Integer.parseInt(arg2);
                            regStore(arg1,value);
                            break;
                        case SVMParser.LOADW:
                            offset = Integer.parseInt(arg2);
                            address = offset + regRead(arg3);
                            regStore(arg1, memory.read(address));
                            break;
                        case SVMParser.MOVE:
                            value = regRead(arg1);
                            regStore(arg2, value);
                            break;

                        case SVMParser.BRANCH:
                            address = Integer.parseInt(code[ip].getArg1());
                            ip = address;
                            break;
                        case SVMParser.BCOND:
                            address = Integer.parseInt(code[ip].getArg1());
                            ip++;
                            value = regRead(bytecode.getArg1());
                            if (value!=0) ip = address;
                            break;

                        case SVMParser.JAL:
                            regStore("$ra", ip);
                            address = Integer.parseInt(code[ip].getArg1());
                            ip = address;
                            break;
                        case SVMParser.JR:
                            ip = regRead(arg1);
                            break;


                        case SVMParser.EQ:
                            regStore(arg1, regRead(arg2)==regRead(arg3)?1:0);
                            break;

                        case SVMParser.LE:
                            regStore(arg1, regRead(arg2)<=regRead(arg3)?1:0);
                            break;
                        case SVMParser.LT:
                            regStore(arg1, regRead(arg2)<regRead(arg3)?1:0);
                            break;
                        case SVMParser.GE:
                            regStore(arg1, regRead(arg2)>=regRead(arg3)?1:0);
                            break;
                        case SVMParser.GT:
                            regStore(arg1, regRead(arg2)>regRead(arg3)?1:0);
                            break;

                        case SVMParser.NOT:
                            regStore(arg1, regRead(arg2) != 0 ? 0 : 1);
                            break;
                        case SVMParser.OR:
                            regStore(arg1, (regRead(arg2)>0 || regRead(arg3)>0) ?1:0);
                            break;
                        case SVMParser.AND:
                            regStore(arg1, (regRead(arg2)>0 && regRead(arg3)>0) ?1:0);
                            break;

                        case SVMParser.NEW:
                            address = memory.allocate();

                            if (address >= hp) hp = address + 1;
                            if (address == -1 || hp > sp) {
                                System.out.println("Memory is full!!");
                                return;
                            }
                            regStore(arg1, address);
                            break;
                        case SVMParser.FREE:
                            address = regRead(arg1);
                            if (address == hp - 1)
                                hp--;
                            memory.free(address);
                            break;

                        case SVMParser.PRINT:
                            if (arg1==null)
                                System.out.println((sp < MEMORY_SIZE) ? memory.read(sp) : "Empty stack!");
                            else{
                                System.out.println( "Print: "+ regRead(arg1));
                            }
                            break;

                        case SVMParser.HALT:
                            //to print the result
                            System.out.println("Halting program...");
                            //printStack(20);
                            //System.out.println("\nResult: " + memory.read(sp) + "\n");

                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Program stopped at program counter: "+ip);
                    /*
                    String toPrint = "";
                    int cont = 0;
                    for (Instruction ins:code){
                        if(ins == null)
                            break;
                        if(cont > ip -10){
                            String literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
                            String str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");
                            toPrint += cont+": "+ str +"\n";
                            //break;
                        }
                        else if(cont == ip){
                            String literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
                            String str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");
                            toPrint += cont+": "+ str +"\n";
                            break;
                        }
                        cont++;

                    }
                    */

                    e.printStackTrace();
                    //printStack(30);
                    return;
                }
            }
        }
    }

    private Integer pop() throws Exception {
        Integer val = memory.read(sp);
        regStore("$sp",sp+1);

        return val;
    }

    private int regRead(String reg) {

        switch (reg) {
            case "$fp":
                return fp;
            case "$bsp":
                return bsp;
            case "$al":
                return al;
            case "$sp":
                return sp;
            case "$ra":
                return ra;

            default:
                switch (reg.charAt(1)) {
                    case 'r':
                        return r[Integer.parseInt(reg.substring(2))];
                    case 'a':
                        return a[Integer.parseInt(reg.substring(2))];

                }
                break;
        }
        return 0;
    }

    private void regStore(String reg, int v) throws Exception {
        switch (reg) {
            case "$fp":
                fp = v;
                break;
            case "$bsp":
                bsp = v;
                break;
            case "$al":
                al = v;
                break;
            case "$sp":
                if (v > sp) {
                    memory.cleanMemory(sp, v);
                }
                sp = v;
                if (sp <= hp) {
                    throw new Exception("Stack overflow!");
                }
                break;
            case "$ra":
                ra = v;
                break;
            default:
                switch (reg.charAt(1)) {
                    case 'r':
                        r[Integer.parseInt(reg.substring(2))] = v;
                        break;
                    case 'a':
                        a[Integer.parseInt(reg.substring(2))] = v;

                        break;
                }
                break;
        }

    }

    private void push(int v) throws Exception {
        regStore("$sp",sp-1);
        memory.write(sp, v);
    }

    private boolean isRegister(String str) {
        Pattern p = Pattern.compile("\\$(([ar][0-9])|(sp)|(fp)|(hp)|(al)|(ra)|(bsp))");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /*private void printStack(int numberOfVarToPrint) {
        int ind = MEMORY_SIZE-1;
        int to = ind-numberOfVarToPrint;
        System.out.println("Actual stack pointer: "+sp+"\Starting print stack:\n");

        while (ind > to){
            System.out.println("Cell: "+ ind + "\tValue: "+memory.read(ind));
            ind--;
        }
        System.out.println("End print stack, current stack pointer: "+sp);

    }*/
}