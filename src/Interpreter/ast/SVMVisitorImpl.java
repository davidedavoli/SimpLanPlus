package Interpreter.ast;

import java.util.HashMap;

import Interpreter.SVM;
import Interpreter.lexer.SVMLexer;
import Interpreter.parser.SVMParser;

public class SVMVisitorImpl extends SVMBaseVisitor<Void> {

	public Instruction[] code = new Instruction[SVM.CODESIZE];
	private int i = 0;
	private HashMap<String,Integer> labelAdd = new HashMap<String,Integer>();
	private HashMap<Integer,String> labelRef = new HashMap<Integer,String>();

	@Override
	public Void visitAssembly(SVMParser.AssemblyContext ctx) {
		visitChildren(ctx);

		for (Integer refAdd: labelRef.keySet()) {
			code[refAdd] = new Instruction(SVMParser.ADDRESS, Integer.toString(labelAdd.get(labelRef.get(refAdd))));
		}

		/*System.out.println("LABEL REF");
		System.out.println(labelRef);
		System.out.println("LABEL ADD");
		System.out.println(labelAdd);
		System.out.println("CODE GEN");
		String toPrint = "";

		for (int i = 0;i<10000;i++){
			Instruction ins = code[i];
			if(ins != null){
				String literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
				String str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");
				toPrint += i+": "+ str +"\n";
			}
		}

		//System.out.println(toPrint);
		for (Integer refAdd: labelRef.keySet()) {
			Instruction ins = code[refAdd];
			String literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
			String str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");
			System.out.println("ReF STR " + str);
			ins = code[Integer.parseInt(ins.getArg1())];
			literalName = SVMParser._LITERAL_NAMES[ins.getCode()];
			str = literalName +" "+(ins.getArg1()!=null?ins.getArg1():"") +" "+(ins.getArg2()!=null?ins.getArg2():"")+" "+(ins.getArg3()!=null?ins.getArg3():"");

			System.out.println("ADDR ARRIVING " + str);
		}*/
		return null;
	}

	@Override
	public Void visitInstruction(SVMParser.InstructionContext ctx) {
		switch (ctx.getStart().getType()) {
			case SVMLexer.PUSH:
				if(ctx.n != null) {
					code[i++] = new Instruction(SVMParser.PUSH, ctx.n.getText());
				}
				else if(ctx.r1 != null){
					code[i++] = new Instruction(SVMParser.PUSH, ctx.r1.getText());
				}
				break;
			case SVMLexer.POP:
				if (ctx.r1!=null){
					code[i++] = new Instruction(SVMParser.POP,  ctx.r1.getText());
				}
				else{
					code[i++] = new Instruction(SVMParser.POP);
				}
				break;
			case SVMLexer.ADD:
				code[i++] = new Instruction(SVMParser.ADD,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.ADDI:
				code[i++] = new Instruction(SVMParser.ADDI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
				break;
			case SVMLexer.SUB:
				code[i++] = new Instruction(SVMParser.SUB,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.SUBI:
				code[i++] = new Instruction(SVMParser.SUBI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
				break;
			case SVMLexer.MULT:
				code[i++] = new Instruction(SVMParser.MULT,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.MULTI:
				code[i++] = new Instruction(SVMParser.MULTI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
				break;
			case SVMLexer.DIV:
				code[i++] = new Instruction(SVMParser.DIV,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.DIVI:
				code[i++] = new Instruction(SVMParser.DIVI,  ctx.r1.getText(), ctx.r2.getText(), ctx.n.getText());
				break;
			case SVMLexer.NOT:
				code[i++] = new Instruction(SVMParser.NOT,  ctx.r1.getText(), ctx.r2.getText());
				break;
			case SVMLexer.OR:
				code[i++] = new Instruction(SVMParser.OR,  ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.LOAD:
				code[i++] = new Instruction(SVMParser.LOAD,  ctx.r1.getText(), ctx.n.getText());
				break;
			case SVMLexer.STOREW:
				code[i++] = new Instruction(SVMParser.STOREW,  ctx.r1.getText(), ctx.o.getText(), ctx.r2.getText());
				break;
			case SVMLexer.LOADW:
				code[i++] = new Instruction(SVMParser.LOADW,  ctx.r1.getText(), ctx.o.getText(), ctx.r2.getText());
				break;
			case SVMLexer.MOVE:
				code[i++] = new Instruction(SVMParser.MOVE, ctx.r1.getText(), ctx.r2.getText());
				break;
			case SVMLexer.LABEL:
				labelAdd.put(ctx.l.getText(), i);
				break;
			case SVMLexer.BRANCH:
				code[i++] = new Instruction(SVMParser.BRANCH, ctx.l.getText());
				labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
			case SVMLexer.BCOND:
				code[i++] = new Instruction(SVMParser.BCOND, ctx.r1.getText(), ctx.l.getText());
				labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
			case SVMLexer.EQ:
				code[i++] = new Instruction(SVMParser.EQ, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.LE:
				code[i++] = new Instruction(SVMParser.LE, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.LT:
				code[i++] = new Instruction(SVMParser.LT, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.GE:
				code[i++] = new Instruction(SVMParser.GE, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.GT:
				code[i++] = new Instruction(SVMParser.GT, ctx.r1.getText(), ctx.r2.getText(), ctx.r3.getText());
				break;
			case SVMLexer.JAL:
				code[i++] = new Instruction(SVMParser.JAL, ctx.l.getText());
				labelRef.put(i++,(ctx.l!=null? ctx.l.getText():null));
				break;
			case SVMLexer.JR:
				code[i++] = new Instruction(SVMParser.JR, ctx.r1.getText());
				break;
			case SVMLexer.FREE:
				code[i++] = new Instruction(SVMParser.FREE, ctx.r1.getText());
				break;
			case SVMLexer.NEW:
				code[i++] = new Instruction(SVMParser.NEW, ctx.r1.getText());
				break;
			case SVMLexer.PRINT:
				if (ctx.r1 == null)
					code[i++] = new Instruction(SVMParser.PRINT);
				else
					code[i++] = new Instruction(SVMParser.PRINT, ctx.r1.getText());
				break;
			case SVMLexer.HALT:
				code[i++] = new Instruction(SVMParser.HALT);
				break;
			default:
				break;	// Invalid instruction
		}
		return null;
	}

}
