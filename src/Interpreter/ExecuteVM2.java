package Interpreter;

import ast.Instruction;
import parser.SVM2Parser;

public class ExecuteVM2 {

    public static final int CODESIZE = 10000;
    public static final int MEMSIZE = 10000;

    private Instruction[] code;
    private int[] memory = new int[MEMSIZE];

    private int ip = 0;             // instruction pointer
    private int sp = MEMSIZE;       // stack pointer
    private int hp = 0;             // heap pointer
    private int fp = MEMSIZE;       // frame pointer
    private int ra;
    private int rv;

    private int[] a = new int[10];
    private int[] r = new int[10];

    public ExecuteVM2(Instruction[] code) {
      this.code = code;
    }
    
    public void cpu() {
      while ( true ) {
    	if(hp+1>=sp) {
    		System.out.println("\nError: Out of memory");
            return;
    	}
    	else {
    		Instruction bytecode = code[ip++]; // fetch
            int v1,v2;
            String arg1, arg2, arg3;
            int address;
            switch ( bytecode.getCode() ) {
              case SVM2Parser.PUSH:
                if (bytecode.getArg1().charAt(0) == 'r' || bytecode.getArg1().charAt(0) == 'a')
                    push(regRead(bytecode.getArg1()));
                else
                  push( Integer.parseInt(bytecode.getArg1()));
                break;
              case SVM2Parser.POP:
                  if (bytecode.getArg1()!= null)
                      regStore(bytecode.getArg1(), pop());
                  else
                      pop();
                    break;
              case SVM2Parser.ADD :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg1();
                  regStore(arg1, regRead(arg2)+regRead(arg3));
                  break;
              case SVM2Parser.ADDI :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)+Integer.parseInt(arg3));
                  break;
              case SVM2Parser.SUB :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)-regRead(arg3));
                  break;
              case SVM2Parser.SUBI :
                 arg1= bytecode.getArg1();
                 arg2= bytecode.getArg2();
                 arg3= bytecode.getArg3();
                 regStore(arg1, regRead(arg2)-Integer.parseInt(arg3));
                 break;
              case SVM2Parser.MULT :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)*regRead(arg3));
                  break;
              case SVM2Parser.MULTI :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)*Integer.parseInt(arg3));
                  break;
              case SVM2Parser.DIV :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)/regRead(arg3));
                  break;
              case SVM2Parser.DIVI :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  arg3= bytecode.getArg3();
                  regStore(arg1, regRead(arg2)/Integer.parseInt(arg3));
                  break;
              case SVM2Parser.NOT :
                  arg1= bytecode.getArg1();
                  arg2= bytecode.getArg2();
                  regStore(arg1, regRead(arg2)!=0?0:1);
                  break;
              case SVM2Parser.STOREW : //
                  arg1 = bytecode.getArg1();
                  v1 = Integer.parseInt(bytecode.getArg2());
                  arg3 = bytecode.getArg3();
                  memory[v1+regRead(arg3)] = regRead(arg1);
                  break;
              case SVM2Parser.LOAD:
                  regStore(bytecode.getArg1(), Integer.parseInt(bytecode.getArg2()));
                  break;
              case SVM2Parser.LOADW : //
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
                regStore(arg1, memory[v1+regRead(arg3)]);
                break;
              case SVM2Parser.BRANCH :
                address = Integer.parseInt(code[ip].getArg1());
                ip = address;
                break;
              case SVM2Parser.BRANCHEQ : //
                address = Integer.parseInt(code[ip].getArg1());
                ip++;  //aumentiamo ip, in caso non venga effettuato il branch
                v1=regRead(bytecode.getArg1());
                v2=regRead(bytecode.getArg2());
                if (v2 == v1) ip = address;
                break;
              case SVM2Parser.BRANCHLESSEQ :
                address = Integer.parseInt(code[ip].getArg1());
                ip++;  //aumentiamo ip, in caso non venga effettuato il branch
                v1=regRead(bytecode.getArg1());
                v2=regRead(bytecode.getArg2());
                if (v1 <= v2) ip = address;
                break;
              case SVM2Parser.BRANCHLESS :
                address = Integer.parseInt(code[ip].getArg1());
                ip++;  //aumentiamo ip, in caso non venga effettuato il branch
                v1=regRead(bytecode.getArg1());
                v2=regRead(bytecode.getArg2());
                if (v1 < v2) ip = address;
                break;
              case SVM2Parser.NEW :
                System.out.println("not implemented yet");
                break;
              case SVM2Parser.FREE:
                System.out.println("not implemented yet");
                break;
              case SVM2Parser.PRINT :
                System.out.println((sp<MEMSIZE)?memory[sp]:"Empty stack!");
                break;
               case SVM2Parser.HALT :
             	//to print the result
             	System.out.println("\nResult: " + memory[sp] + "\n");
             	return;
            }
    	} 
      }
    } 
    
    private int pop() {
      return memory[sp++];
    }

    private int regRead(String reg){
        switch (reg.charAt(0)){
            case 'r':
                return r[Integer.parseInt(reg.substring(1))];
            case 'a':
                return a[Integer.parseInt(reg.substring(1))];
            default:
                if (reg=="fp")
                    return fp;
                else if (reg=="sp")
                    return sp;
        }
        return 0;
    }

    private void regStore(String reg, int v){
        switch (reg.charAt(0)){
            case 'r':
                r[Integer.parseInt(reg.substring(1))]=v;
            case 'a':
                a[Integer.parseInt(reg.substring(1))]=v;
            default:
                if (reg=="fp")
                    fp=v;
                else if (reg=="sp")
                    sp=v;
        }
    }

    private void push(int v) {
      memory[--sp] = v;
    }
    
}