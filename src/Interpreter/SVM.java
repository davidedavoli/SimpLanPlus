package Interpreter;

import Interpreter.ast.Instruction;
import Interpreter.memory.Memory;
import Interpreter.parser.SVMParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVM {

    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;

    private Instruction[] code;
    private Memory memory = new Memory(MEMSIZE);

    private int ip = 0;             // instruction pointer, internal register, no write nor read
    private int sp = MEMSIZE;       // stack pointer
    private int hp = 0;             // heap pointer read-only
    private int fp = MEMSIZE;       // frame pointer
    private int ra;
    private int rv;

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
                int v1, v2;
                String arg1, arg2, arg3;
                int address;
                try {
                    switch (bytecode.getCode()) {
                        case SVMParser.PUSH:
                            if (isRegister(bytecode.getArg1()))
                                push(regRead(bytecode.getArg1()));
                            else
                                push(Integer.parseInt(bytecode.getArg1()));
                            break;
                        case SVMParser.POP:
                            if (bytecode.getArg1() != null && isRegister(bytecode.getArg1()))
                                regStore(bytecode.getArg1(), pop());
                            else
                                pop();
                            break;
                        case SVMParser.ADD:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg1();
                            regStore(arg1, regRead(arg2) + regRead(arg3));
                            break;
                        case SVMParser.ADDI:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) + Integer.parseInt(arg3));
                            break;
                        case SVMParser.SUB:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) - regRead(arg3));
                            break;
                        case SVMParser.SUBI:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) - Integer.parseInt(arg3));
                            break;
                        case SVMParser.MULT:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) * regRead(arg3));
                            break;
                        case SVMParser.MULTI:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) * Integer.parseInt(arg3));
                            break;
                        case SVMParser.DIV:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) / regRead(arg3));
                            break;
                        case SVMParser.DIVI:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, regRead(arg2) / Integer.parseInt(arg3));
                            break;
                        case SVMParser.NOT:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            regStore(arg1, regRead(arg2) != 0 ? 0 : 1);
                            break;
                        case SVMParser.OR:
                            arg1 = bytecode.getArg1();
                            arg2 = bytecode.getArg2();
                            arg3 = bytecode.getArg3();
                            regStore(arg1, (regRead(arg2)>0 || regRead(arg3)>0) ?1:0);
                            break;
                        case SVMParser.STOREW: //
                            arg1 = bytecode.getArg1();
                            v1 = Integer.parseInt(bytecode.getArg2());
                            arg3 = bytecode.getArg3();
                            //                  memory[v1+regRead(arg3)] = regRead(arg1);
                            memory.write(v1 + regRead(arg3), regRead(arg1));

                            break;
                        case SVMParser.LOAD:
                            regStore(bytecode.getArg1(),
                                    Integer.parseInt(bytecode.getArg2()));
                            break;
                        case SVMParser.LOADW: //
                            // check if object address where we take the method label
                            // is null value (-10000) //TODO l'if è un fossile del codice di Simplan, si valuti se tenerlo o rimuoverlo
                    /*
                    if (memory[sp] == -10000) {
                        System.out.println("\nError: Null pointer exception");
                        return;
                    }
                     */
                            arg1 = bytecode.getArg1();
                            v1 = Integer.parseInt(bytecode.getArg2());
                            arg3 = bytecode.getArg3();
                            //                regStore(arg1, memory[v1+regRead(arg3)]);
                            regStore(arg1, memory.read(v1 + regRead(arg3)));
                            break;
                        case SVMParser.BRANCH:
                            address = Integer.parseInt(code[ip].getArg1());
                            ip = address;
                            break;
                        case SVMParser.BCOND: //
                            address = Integer.parseInt(code[ip].getArg2());
                            ip++;  //aumentiamo ip, in caso non venga effettuato il branch
                            v1 = regRead(bytecode.getArg1());
                            if (v1!=0) ip = address;
                            break;
                        case SVMParser.EQ:
                            regStore(bytecode.getArg1(), regRead(bytecode.getArg2())==regRead(bytecode.getArg3())?1:0);
                            break;
                        case SVMParser.LE:
                            regStore(bytecode.getArg1(), regRead(bytecode.getArg2())<=regRead(bytecode.getArg3())?1:0);
                            break;
                        case SVMParser.LT:
                            regStore(bytecode.getArg1(), regRead(bytecode.getArg2())<regRead(bytecode.getArg3())?1:0);
                            break;
                        case SVMParser.GE:
                            regStore(bytecode.getArg1(), regRead(bytecode.getArg2())>=regRead(bytecode.getArg3())?1:0);
                            break;
                        case SVMParser.GT:
                            regStore(bytecode.getArg1(), regRead(bytecode.getArg2())>regRead(bytecode.getArg3())?1:0);
                            break;
                        case SVMParser.NEW:
                            address = memory.allocate();
                            if (address >= hp) hp = address + 1;
                            if (address == -1 || hp > sp) {
                                System.out.println("Memory is full!!");
                                return;
                            }
                            regStore(bytecode.getArg1(), address);
                            break;
                        case SVMParser.FREE:
                            address = regRead(bytecode.getArg1());
                            if (address == hp - 1)
                                hp--; //se è l'ultimo indirizzo occupato, allora hp viene decrementato
                            memory.free(address);
                            break;
                        case SVMParser.PRINT:
                            if (bytecode.getArg1()==null)
                                System.out.println((sp < MEMSIZE) ? memory.read(sp) : "Empty stack!");
                            else
                                System.out.println((sp < MEMSIZE) ? regRead(bytecode.getArg1()) : "Empty stack!");

                            break;
                        case SVMParser.HALT:
                            //to print the result
                            System.out.println("\nResult: " + memory.read(sp) + "\n");
                            return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private int pop() {
        return memory.read(sp++);
    }

    private int regRead(String reg) {
        switch (reg.charAt(0)) {
            case 'r':
                return r[Integer.parseInt(reg.substring(1))];
            case 'a':
                return a[Integer.parseInt(reg.substring(1))];
            default:
                if (reg.equals("$fp"))
                    return fp;
                else if (reg.equals("$sp"))
                    return sp;
                else if (reg.equals("$hp")) {
                    return hp;
                }
        }
        return 0;
    }

    private void regStore(String reg, int v) throws Exception {
        switch (reg.charAt(0)) {
            case 'r':
                r[Integer.parseInt(reg.substring(1))] = v;
            case 'a':
                a[Integer.parseInt(reg.substring(1))] = v;
            default:
                if (reg.equals("$fp"))
                    fp = v;
                else if (reg.equals("$sp")) {
                    sp = v;
                    if (sp <= hp) {
                        throw new Exception("Stack overflow!");
                    }
                }
        }
    }

    private void push(int v) {
        memory.write(--sp, v);
    }

    private boolean isRegister(String str) {
        Pattern p = Pattern.compile("\\$(([ar][0-9])|(sp)|(fp)|(hp))");
        Matcher m = p.matcher(str);
        return m.matches();
    }
}