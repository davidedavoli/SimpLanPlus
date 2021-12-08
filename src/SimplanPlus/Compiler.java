package SimplanPlus;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

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


public class Compiler {
	private final static String asmDir = "asm/";


	private static CommonTokenStream lexer(String codeFile) throws IOException {
		FileInputStream is = new FileInputStream(codeFile);
		ANTLRInputStream input = new ANTLRInputStream(is);
		SimpLanPlusLexer lexer = new SimpLanPlusLexer(input);

		return new CommonTokenStream(lexer);
	}
	private static Node parser(CommonTokenStream tokens){
		SimpLanPlusParser parser = new SimpLanPlusParser(tokens);
		SimpLanPlusVisitorImpl visitor = new SimpLanPlusVisitorImpl();

		return visitor.visitMainBlock(parser.block(), true);
	}
	private static void checkErrorAst(Node ast, Environment env) {
		//SIMPLE CHECK FOR LEXER ERRORS

		ArrayList<SemanticError> semanticError = ast.checkSemantics(env);

		System.out.println("You had: "+semanticError.size()+" semantic errors.");
		if(semanticError.size()>0){
			semanticError.stream().filter(KeySet.keySet(SemanticError::toString)).forEach(System.err::println);
			System.exit(1);
		}
		//System.out.println("Visualizing AST...");
		//System.out.println(ast.toPrint(""));

	}

	private static void typeCheck(Node ast) {
		TypeNode type = ast.typeCheck(); //type-checking bottom-up
		System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
	}

	private static void checkEffects(Node ast, Environment env){
		ArrayList<EffectError> effectErrors = ast.checkEffects(env);
		System.out.println("You had: "+effectErrors.size()+" effects errors.");
		if(effectErrors.size()>0){
			effectErrors.stream().filter(KeySet.keySet(EffectError::toString)).forEach(System.err::println);
			System.exit(1);
		}
	}

	private static void codeGeneration(String fileAsm, Node ast) throws IOException {

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


	private static Instruction[] compile(String fileAbsoluteName, String fileName) throws IOException {
		String fileAsm = asmDir+fileName+".asm";
		CommonTokenStream tokens = lexer(fileAbsoluteName);

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
		return visitorSVM.getCode();

	}

	private static void interpreter(Instruction[] code, String filename) {
		System.out.println("Starting Virtual Machine for "+filename+"...");
		SVM vm = new SVM(code);
		vm.cpu();
	}

	public static void main(String[] args) {
		try {
			/* Starting compiler */
			System.out.println("### SimpLanPlus Compiler&Interpreter ###");
			if(args.length == 0){
				System.err.println("No file to compile & run provided.");
				System.exit(0);
			}
			String file = args[0];

			if(!Paths.get(file).toFile().exists()) {
				throw new FileNotFoundException("File: " + file + " not found.");
			}
			System.out.println("File to compile:\t" + file);

			String[] path = file.split(Pattern.quote(File.separator));
			String name = path[path.length-1];

			Instruction[] code = compile(file,name);

			interpreter(code,name);
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
			System.exit(2);
		}

	}


}
