// Generated from /home/davide/Codice/SimpLanPlus/src/parser/SVM2.g4 by ANTLR 4.9.1
package parser;

import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SVM2Parser}.
 */
public interface SVM2Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SVM2Parser#assembly}.
	 * @param ctx the parse tree
	 */
	void enterAssembly(SVM2Parser.AssemblyContext ctx);
	/**
	 * Exit a parse tree produced by {@link SVM2Parser#assembly}.
	 * @param ctx the parse tree
	 */
	void exitAssembly(SVM2Parser.AssemblyContext ctx);
	/**
	 * Enter a parse tree produced by {@link SVM2Parser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(SVM2Parser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SVM2Parser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(SVM2Parser.InstructionContext ctx);
}