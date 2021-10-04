package ast;

import java.util.HashMap;

import Interpreter.ExecuteVM2;
import parser.*;

public class SVM2VisitorImpl extends SVM2BaseVisitor<Void> {

    public Instruction[] code = new Instruction[ExecuteVM2.CODESIZE];
    private int i = 0;
    private HashMap<String,Integer> labelAdd = new HashMap<String,Integer>();
    private HashMap<Integer,String> labelRef = new HashMap<Integer,String>();

    @Override
    public Void visitAssembly(SVM2Parser.AssemblyContext ctx) {
        visitChildren(ctx);

        for (Integer refAdd: labelRef.keySet()) {
            code[refAdd] = new Instruction(SVM2Parser.ADDRESS, Integer.toString(labelAdd.get(labelRef.get(refAdd))));
        }
        return null;
    }

    @Override
    public Void visitInstruction(SVM2Parser.InstructionContext ctx) {
        switch (ctx.getStart().getType()) {
            case SVM2Lexer.PUSH:
                if(ctx.n != null) {
                    code[i++] = new Instruction(SVM2Parser.PUSH, ctx.n.getText());
                }
                else if(ctx.r1 != null){
                    code[i++] = new Instruction(SVM2Parser.PUSH, ctx.r1.getText());
                }
                break;
            case SVM2Lexer.POP:
                if (ctx.r1!=null){
                    code[i++] = new Instruction(SVM2Parser.POP,  ctx.r1.getText());
                }
                else{
                    code[i++] = new Instruction(SVM2Parser.POP);
                }
                break;
            case SVM2Lexer.ADD:
                code[i++] = new Instruction(SVM2Parser.ADD,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.ADDI:
                code[i++] = new Instruction(SVM2Parser.ADDI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
                break;
            case SVM2Lexer.SUB:
                code[i++] = new Instruction(SVM2Parser.SUB,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.SUBI:
                code[i++] = new Instruction(SVM2Parser.SUBI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
                break;
            case SVM2Lexer.MULT:
                code[i++] = new Instruction(SVM2Parser.MULT,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.MULTI:
                code[i++] = new Instruction(SVM2Parser.MULTI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
                break;
            case SVM2Lexer.DIV:
                code[i++] = new Instruction(SVM2Parser.DIV,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.DIVI:
                code[i++] = new Instruction(SVM2Parser.DIVI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
                break;
            case SVM2Lexer.NOT:
                code[i++] = new Instruction(SVM2Parser.NOT,  ctx.r1.getText(), ctx.r2.getText());
                break;
            case SVM2Lexer.OR:
                code[i++] = new Instruction(SVM2Parser.OR,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.LOAD:
                code[i++] = new Instruction(SVM2Parser.LOAD,  ctx.r1.getText(), ctx.n.getText());
                break;
            case SVM2Lexer.STOREW:
                code[i++] = new Instruction(SVM2Parser.STOREW,  ctx.r1.getText(), ctx.o.getText(), ctx.r2.getText());
                break;
            case SVM2Lexer.LOADW:
                code[i++] = new Instruction(SVM2Parser.LOADW,  ctx.r1.getText(), ctx.o.getText(), ctx.r2.getText());
                break;
            case SVM2Lexer.LABEL:
                labelAdd.put(ctx.l.getText(), i);
                break;
            case SVM2Lexer.BRANCH:
                code[i++] = new Instruction(SVM2Parser.BRANCH, ctx.l.getText());
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
            case SVM2Lexer.BCOND:
                code[i++] = new Instruction(SVM2Parser.BCOND, ctx.r1.getText(), ctx.l.getText());
                labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
                break;
            case SVM2Lexer.EQ:
                code[i++] = new Instruction(SVM2Parser.EQ, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.LE:
                code[i++] = new Instruction(SVM2Parser.LE, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.LT:
                code[i++] = new Instruction(SVM2Parser.LT, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.GE:
                code[i++] = new Instruction(SVM2Parser.GE, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.GT:
                code[i++] = new Instruction(SVM2Parser.GT, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
                break;
            case SVM2Lexer.FREE:
                code[i++] = new Instruction(SVM2Parser.FREE, ctx.r1.getText());
                break;
            case SVM2Lexer.NEW:
                code[i++] = new Instruction(SVM2Parser.NEW, ctx.r1.getText());
                break;
            case SVM2Lexer.PRINT:
                if (ctx.r1 == null)
                    code[i++] = new Instruction(SVM2Parser.PRINT);
                else
                    code[i++] = new Instruction(SVM2Parser.PRINT, ctx.r1.getText());
                break;
            case SVM2Lexer.HALT:
                code[i++] = new Instruction(SVM2Parser.HALT);
                break;
            default:
                break;	// Invalid instruction
        }
        return null;
    }

}
