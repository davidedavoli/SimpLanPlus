package Interpreter;

import Interpreter.ast.Instruction;
import Interpreter.memory.Memory;
import Interpreter.parser.SVMParser;
import ast.STentry;
import ast.node.Node;
import ast.node.dec.VarNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SVM {

    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;

    private final Instruction[] code;
    private final Memory memory = new Memory(MEMSIZE);

    private int ip = 0;             // instruction pointer, internal register, no write nor read
    private int sp = MEMSIZE;       // stack pointer
    private int hp = 0;             // heap pointer read-only
    private int fp = MEMSIZE-1;       // frame pointer
    private int ra;
    private int al;
    private int bsp = MEMSIZE;

    private int[] a = new int[10];
    private int[] r = new int[10];

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
                            int addr_sw = offset + regRead(arg3);
                            memory.write(addr_sw, regRead(arg1));
                            break;
                        case SVMParser.LOAD:
                            value = Integer.parseInt(arg2);
                            regStore(arg1,value);
                            break;
                        case SVMParser.LOADW:
                            // check if object address where we take the method label
                            // is null value (-10000) //TODO l'if è un fossile del codice di Simplan, si valuti se tenerlo o rimuoverlo
                    /*
                    if (memory[sp] == -10000) {
                        System.out.println("\nError: Null pointer exception");
                        return;
                    }
                     */
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
                            ip++;  //aumentiamo ip, in caso non venga effettuato il branch
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
                                hp--; //se è l'ultimo indirizzo occupato, allora hp viene decrementato
                            memory.free(address);
                            break;

                        case SVMParser.PRINT:
                            if (arg1==null)
                                System.out.println((sp < MEMSIZE) ? memory.read(sp) : "Empty stack!");
                            else{
                                System.out.println( "PRINTO IL VALORE NEL REGISTRO: "+arg1 +" CON VALORE: "+ regRead(arg1));
                            }
                            //System.out.println((sp < MEMSIZE) ? regRead(arg1) : "Empty stack!");

                            break;

                        case SVMParser.HALT:
                            //to print the result
                            System.out.println("\nResult: " + memory.read(sp) + "\n");
                            printStack(20);
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("PROGRAMM STOPPED AT "+ip);
                    //Map<String, STentry > hm = env.symTable.get(env.nestingLevel)
                    String toPrint = "";
                    int cont = 0;
                    for (Instruction ins:code){
                        if(ins == null)
                            break;
                        else{
                            String literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
                            String str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");
                            toPrint += cont+": "+ str +"\n";
                        }
                        if(cont == ip)
                            break;
                        cont++;

                    }

                    e.printStackTrace();
                    System.out.println(toPrint);
                    printStack(20);
                    return;
                }
            }
        }
    }

    private int pop() {
        return memory.read(sp++);
    }

    private int regRead(String reg) {

        if (reg.equals("$fp")){
            return fp;
        }
        else if(reg.equals("$bsp")){
            return bsp;
        }
        else if (reg.equals("$al")){
            return al;
        }
        else if (reg.equals("$sp")) {
            return sp;
        }
        else if (reg.equals("$ra")) {
            return ra;

        }
        else{
            switch (reg.charAt(1)) {
                case 'r':
                    return r[Integer.parseInt(reg.substring(2))];
                case 'a':
                    return a[Integer.parseInt(reg.substring(2))];

            }
        }
        return 0;
    }

    private void regStore(String reg, int v) throws Exception {
        if (reg.equals("$fp")){
            fp = v;
        }
        else if(reg.equals("$bsp")){
            bsp = v;
        }
        else if (reg.equals("$al")){
            al = v;
        }
        else if (reg.equals("$sp")) {
            sp = v;
            if (sp <= hp) {
                throw new Exception("Stack overflow!");
            }
        }
        else if (reg.equals("$ra")) {
            ra = v;

        }
        else{
            switch (reg.charAt(1)) {
                case 'r':
                    r[Integer.parseInt(reg.substring(2))] = v;
                    break;
                case 'a':
                    a[Integer.parseInt(reg.substring(2))] = v;

                    break;
            }
        }

    }

    private void push(int v) {
        memory.write(--sp, v);
    }

    private boolean isRegister(String str) {
        Pattern p = Pattern.compile("\\$(([ar][0-9])|(sp)|(fp)|(hp)|(al)|(ra)|(bsp))");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    private void printStack(int numberOfVarToPrint){
        int ind = MEMSIZE-1;
        int to = ind-numberOfVarToPrint;
        System.out.println("Inizio print stack");

        while (ind > to){
            System.out.println("CELL "+ ind + " Value: "+ memory.read(ind));
            ind--;
        }
        System.out.println("Fine print stack, current sp_addr: "+sp);

    }
}