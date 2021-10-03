package mainPackage;

import Interpreter.ExecuteVM;
import Interpreter.ExecuteVM2;
import ast.SVM2VisitorImpl;
import ast.SVMVisitorImpl;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SVM2Lexer;
import parser.SVM2Parser;
import parser.SVMLexer;
import parser.SVMParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestVM {
    public static void main (String argv[]) throws IOException {
        String fileName="VMsample";
        FileInputStream isASM = new FileInputStream(fileName+".asm");
        ANTLRInputStream inputASM = new ANTLRInputStream(isASM);
        SVM2Lexer lexerASM = new SVM2Lexer(inputASM);
        CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
        SVM2Parser parserASM = new SVM2Parser(tokensASM);

        //parserASM.assembly();

        SVM2VisitorImpl visitorSVM = new SVM2VisitorImpl();
        visitorSVM.visit(parserASM.assembly());

        System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors and "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
        if (lexerASM.lexicalErrors>0 || parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);

        System.out.println("Starting Virtual Machine...");
        ExecuteVM2 vm = new ExecuteVM2(visitorSVM.code);
        vm.cpu();

    }
}
