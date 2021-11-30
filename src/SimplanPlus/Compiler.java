package SimplanPlus;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

import Interpreter.SVM;
import Interpreter.ast.Instruction;
import Interpreter.ast.SVMVisitorImpl;
import Interpreter.lexer.SVMLexer;
import Interpreter.parser.SVMParser;
import ast.node.Node;
import ast.node.types.TypeNode;
import lexer.SimpLanPlusLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.SimpLanPlusVisitorImpl;
import parser.SimpLanPlusParser;
import semantic.Environment;
import ast.Label;
import semantic.SemanticError;
import effect.EffectError;
import semantic.SimplanPlusException;


public class Compiler {
	private final static String dir = "examples/";
	private final static String asmDir = "asm/";
	private final static String baseName = "example";
	private final static String ext = ".simplan";


	private static CommonTokenStream lexer(String codeFile) throws IOException {
		FileInputStream is = new FileInputStream(codeFile);
		ANTLRInputStream input = new ANTLRInputStream(is);
		SimpLanPlusLexer lexer = new SimpLanPlusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		return tokens;
	}
	private static Node parser(CommonTokenStream tokens){
		SimpLanPlusParser parser = new SimpLanPlusParser(tokens);
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();

		Node ast = visitor.visitMainBlock(parser.block(), true); //generazione AST
		return ast;
	}
	private static void checkErrorAst(Node ast, Environment env) throws SimplanPlusException {
		//SIMPLE CHECK FOR LEXER ERRORS

		ArrayList<SemanticError> semanticError = ast.checkSemantics(env);

		System.out.println("You had: "+semanticError.size()+" semantic errors.");
		if(semanticError.size()>0){
			for(SemanticError e : semanticError)
				System.out.println("\t" + e);
			System.exit(1);
		}
		//System.out.println("Visualizing AST...");
		//System.out.println(ast.toPrint(""));

	}

	private static void typeCheck(Node ast) throws SimplanPlusException {
		TypeNode type = ast.typeCheck(); //type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
	}

	private static void checkEffects(Node ast, Environment env){
		ArrayList<EffectError> effectErrors = ast.checkEffects(env);
		System.out.println("You had: "+effectErrors.size()+" effects errors.");
		if(effectErrors.size()>0){
			for(EffectError e : effectErrors)
				System.out.println("\t" + e);
			System.exit(1);
		}
	}

	private static void codeGeneration(String fileAsm, Node ast) throws IOException, SimplanPlusException {

		Label labelManager = new Label();
		String code=ast.codeGeneration(labelManager);
		BufferedWriter out = new BufferedWriter(new FileWriter(fileAsm));
		out.write(code);
		out.close();

		System.out.println("Code generated! Assembling and running generated code.");
	}


	private static CommonTokenStream SVMLexer(String fileAsm) throws IOException {
		FileInputStream isASM = new FileInputStream(fileAsm);
		ANTLRInputStream inputASM = new ANTLRInputStream(isASM);
		SVMLexer lexerASM = new SVMLexer(inputASM);
		CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);

		System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors in SVM.");

		if (lexerASM.lexicalErrors>0 ) System.exit(1);

		return tokensASM;
	}

	private static SVMVisitorImpl SVMParser(CommonTokenStream tokensASM){
		SVMParser parserASM = new SVMParser(tokensASM);

		SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
		visitorSVM.visit(parserASM.assembly());

		System.out.println("You had: "+parserASM.getNumberOfSyntaxErrors()+" syntax errors in SVM.");
		if (parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);

		return visitorSVM;
	}

	private static void compileFile(String fileAbsName,String fileName) throws IOException, SimplanPlusException {
		System.out.println("COMPILING "+fileAbsName);
		String fileAsm = asmDir+fileName+".asm";
		CommonTokenStream tokens = lexer(fileAbsName);

		Node ast = parser(tokens);
		Environment env = new Environment();

		/**
		 * Check program error
		 */
		checkErrorAst(ast, env);
		typeCheck(ast);
		checkEffects(ast,env);

		codeGeneration(fileAsm,ast);

		CommonTokenStream tokensASM = SVMLexer(fileAsm);
		SVMVisitorImpl visitorSVM = SVMParser(tokensASM);

		interpreterCode(visitorSVM.getCode(),fileName);
	}

	private static void interpreterCode(Instruction[] code,String filename) throws SimplanPlusException {
		System.out.println("Starting Virtual Machine for "+filename+"...");
		SVM vm = new SVM(code);
		vm.cpu();
	}
	public static void main(String[] args) throws Exception {

		//32 dovrebbe andare bene
		int numberSingle = 4;
		String dire = "tommasoExamples/";
		String fileAbsNameSingle = dire + baseName + numberSingle + ext;
		String fileNameSingle = baseName + numberSingle + ext;
		compileFile(fileAbsNameSingle,fileNameSingle);


		int numberOfTest = Objects.requireNonNull(new File(dire).list()).length;
		//System.out.println("NUMBER OF TEST IS " + numberOfTest);
		for(int number = 1; number<=numberOfTest;number++){
			String fileAbsName = dire + baseName + number + ext;
			String fileName = baseName + number + ext;
		//	compileFile(fileAbsName,fileName);
		}

	}
}
