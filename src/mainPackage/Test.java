package mainPackage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import util.Environment;
import util.Label;
import util.SemanticError;
import util.SimplanPlusException;

import static org.antlr.v4.codegen.DefaultOutputModelFactory.list;

public class Test {
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
	private static ArrayList<SemanticError> checkErrorAst(Node ast) throws SimplanPlusException {
		//SIMPLE CHECK FOR LEXER ERRORS
		Environment env = new Environment();
		ArrayList<SemanticError> err = ast.checkSemantics(env);


		if(err.size()>0){
			return err;
		}
		else{
			System.out.println("Visualizing AST...");
			//System.out.println(ast.toPrint(""));
		}

		return err;
	}

	private static void typeCheck(Node ast) throws SimplanPlusException {
		TypeNode type = ast.typeCheck(); //type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
	}

	private static void codeGeneration(String fileAsm,Node ast) throws IOException, SimplanPlusException {

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

		System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors syntax errors.");
		if (lexerASM.lexicalErrors>0 ) System.exit(1);

		return tokensASM;
	}

	private static SVMVisitorImpl SVMParser(CommonTokenStream tokensASM){
		SVMParser parserASM = new SVMParser(tokensASM);

		SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
		visitorSVM.visit(parserASM.assembly());

		System.out.println("You had: "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
		if (parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);

		return visitorSVM;
	}

	private static void compileFile(String fileAbsName,String fileName) throws IOException, SimplanPlusException {
		System.out.println("COMPILING "+fileAbsName);
		String fileAsm = asmDir+fileName+".asm";
		CommonTokenStream tokens = lexer(fileAbsName);

		Node ast = parser(tokens);

		ArrayList<SemanticError> err = checkErrorAst(ast);

		if(err.size()>0){
			System.out.println("You had: " +err.size()+" errors:");
			for(SemanticError e : err)
				System.out.println("\t" + e);
			return;
		}

		typeCheck(ast);
		codeGeneration(fileAsm,ast);

		CommonTokenStream tokensASM = SVMLexer(fileAsm);
		SVMVisitorImpl visitorSVM = SVMParser(tokensASM);

		interpreterCode(visitorSVM.getCode(),fileName);
	}

	private static void interpreterCode(Instruction[] code,String filename){
		System.out.println("Starting Virtual Machine for "+filename+"...");
		SVM vm = new SVM(code);
		vm.cpu();
	}
	public static void main(String[] args) throws Exception {

		int numberOfTest = Objects.requireNonNull(new File("examples/").list()).length;
		System.out.println("NUMBER OF TEST IS " + numberOfTest);
		for(int number = 1; number<=numberOfTest;number++){
			String fileAbsName = dir + baseName + number + ext;
			String fileName = baseName + number + ext;
			compileFile(fileAbsName,fileName);
		}
	}
}
