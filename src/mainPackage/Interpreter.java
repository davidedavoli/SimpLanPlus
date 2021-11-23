package mainPackage;

import Interpreter.SVM;
import Interpreter.ast.SVMVisitorImpl;
import Interpreter.lexer.SVMLexer;
import Interpreter.parser.SVMParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import semantic.SimplanPlusException;

import java.io.FileInputStream;
import java.io.IOException;

public class Interpreter {
    public static void main (String argv[]) throws IOException, SimplanPlusException {
        String fileName="VMsample";
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
        SVM vm = new SVM(visitorSVM.getCode());
        vm.cpu();

    }
}
