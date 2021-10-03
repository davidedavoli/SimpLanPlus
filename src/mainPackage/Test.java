package mainPackage;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import Interpreter.ExecuteVM;
import ast.Node;
import ast.SVMVisitorImpl;
import ast.SimpLanPlusVisitorImpl;
import parser.SVMLexer;
import parser.SVMParser;
import parser.SimpLanPlusLexer;
import parser.SimpLanPlusParser;
import types.TypeNode;
import util.Environment;
import util.SemanticError;

public class Test {
	public static void main(String[] args) throws Exception {

		String fileName = "prova.simplan";

		FileInputStream is = new FileInputStream(fileName);
		ANTLRInputStream input = new ANTLRInputStream(is);
		SimpLanPlusLexer lexer = new SimpLanPlusLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		SimpLanPlusParser parser = new SimpLanPlusParser(tokens);
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();
		Node ast = visitor.visit(parser.block()); //generazione AST 
			
		//SIMPLE CHECK FOR LEXER ERRORS
		Environment env = new Environment();	
		ArrayList<SemanticError> err = ast.checkSemantics(env);
			if(err.size()>0){
				System.out.println("You had: " +err.size()+" errors:");
				for(SemanticError e : err)
					System.out.println("\t" + e);
				return;
			}
			
		
		System.out.println("Visualizing AST...");
		System.out.println(ast.toPrint(""));

		TypeNode type = ast.typeCheck(); //type-checking bottom-up 
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));


		// CODE GENERATION  prova.SimpLan.asm
		String code=ast.codeGeneration(); 
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName+".asm")); 
		out.write(code);
		out.close(); 
		System.out.println("Code generated! Assembling and running generated code.");

		FileInputStream isASM = new FileInputStream(fileName+".asm");
		ANTLRInputStream inputASM = new ANTLRInputStream(isASM);
		SVMLexer lexerASM = new SVMLexer(inputASM);
		CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
		SVMParser parserASM = new SVMParser(tokensASM);

		//parserASM.assembly();

		SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
		visitorSVM.visit(parserASM.assembly());

		System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors and "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
		if (lexerASM.lexicalErrors>0 || parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);

		System.out.println("Starting Virtual Machine...");
		ExecuteVM vm = new ExecuteVM(visitorSVM.code);
		vm.cpu();


	}
}