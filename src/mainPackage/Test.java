package mainPackage;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import Interpreter.SVM;
import Interpreter.lexer.SVMLexer;
import Interpreter.parser.SVMParser;
import Interpreter.ast.SVMVisitorImpl;
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

public class Test {
	private Label labelManager = new Label();

	public Label getlabelManager(){
		return labelManager;
	}
	public void main(String[] args) throws Exception {

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

		this.labelManager = new Label();
		// CODE GENERATION  prova.SimpLan.asm
		String code=ast.codeGeneration(this.labelManager);
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
		SVM vm = new SVM(visitorSVM.code);
		vm.cpu();


	}
}
