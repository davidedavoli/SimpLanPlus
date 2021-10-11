// Generated from D:/universita/SimpLanPlus/src/Interpreter/lexer\SVM.g4 by ANTLR 4.9.1
package Interpreter.lexer;

import java.util.HashMap;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REGISTER=1, PUSH=2, ADDRESS=3, POP=4, ADD=5, ADDI=6, SUB=7, SUBI=8, MULT=9, 
		MULTI=10, DIV=11, DIVI=12, NOT=13, OR=14, STOREW=15, LOADW=16, MOVE=17, 
		BRANCH=18, BCOND=19, LE=20, LT=21, EQ=22, GE=23, GT=24, PRINT=25, LOAD=26, 
		HALT=27, FREE=28, NEW=29, COL=30, LPAR=31, RPAR=32, LABEL=33, NUMBER=34, 
		WHITESP=35, LINECOMMENTS=36, BLOCKCOMMENTS=37, ERR=38;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"REGISTER", "PUSH", "ADDRESS", "POP", "ADD", "ADDI", "SUB", "SUBI", "MULT", 
			"MULTI", "DIV", "DIVI", "NOT", "OR", "STOREW", "LOADW", "MOVE", "BRANCH", 
			"BCOND", "LE", "LT", "EQ", "GE", "GT", "PRINT", "LOAD", "HALT", "FREE", 
			"NEW", "COL", "LPAR", "RPAR", "LABEL", "NUMBER", "WHITESP", "LINECOMMENTS", 
			"BLOCKCOMMENTS", "ERR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'push'", "'address'", "'pop'", "'add'", "'addi'", "'sub'", 
			"'subi'", "'mult'", "'multi'", "'div'", "'divi'", "'not'", "'or'", "'sw'", 
			"'lw'", "'mv'", "'b'", "'bc'", "'le'", "'lt'", "'eq'", "'ge'", "'gt'", 
			"'print'", "'li'", "'halt'", "'free'", "'new'", "':'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "REGISTER", "PUSH", "ADDRESS", "POP", "ADD", "ADDI", "SUB", "SUBI", 
			"MULT", "MULTI", "DIV", "DIVI", "NOT", "OR", "STOREW", "LOADW", "MOVE", 
			"BRANCH", "BCOND", "LE", "LT", "EQ", "GE", "GT", "PRINT", "LOAD", "HALT", 
			"FREE", "NEW", "COL", "LPAR", "RPAR", "LABEL", "NUMBER", "WHITESP", "LINECOMMENTS", 
			"BLOCKCOMMENTS", "ERR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public int lexicalErrors=0;


	public SVMLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 37:
			ERR_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void ERR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 System.err.println("Invalid char: "+ getText()); lexicalErrors++;  
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2(\u0116\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2]\n\2\5\2_\n\2\3\3\3\3\3\3\3\3\3\3\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36"+
		"\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\7\"\u00db\n\"\f\"\16\"\u00de\13\""+
		"\3#\3#\5#\u00e2\n#\3#\3#\7#\u00e6\n#\f#\16#\u00e9\13#\5#\u00eb\n#\3$\6"+
		"$\u00ee\n$\r$\16$\u00ef\3$\3$\3%\3%\3%\3%\7%\u00f8\n%\f%\16%\u00fb\13"+
		"%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\7&\u0108\n&\f&\16&\u010b\13&\3&\3&"+
		"\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\2\2(\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n"+
		"\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30"+
		"/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(\3\2\n\4\2cct"+
		"t\4\2C\\c|\5\2\62;C\\c|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2,,\61\61\3"+
		"\2,,\3\2\61\61\2\u0124\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5"+
		"`\3\2\2\2\7e\3\2\2\2\tm\3\2\2\2\13q\3\2\2\2\ru\3\2\2\2\17z\3\2\2\2\21"+
		"~\3\2\2\2\23\u0083\3\2\2\2\25\u0088\3\2\2\2\27\u008e\3\2\2\2\31\u0092"+
		"\3\2\2\2\33\u0097\3\2\2\2\35\u009b\3\2\2\2\37\u009e\3\2\2\2!\u00a1\3\2"+
		"\2\2#\u00a4\3\2\2\2%\u00a7\3\2\2\2\'\u00a9\3\2\2\2)\u00ac\3\2\2\2+\u00af"+
		"\3\2\2\2-\u00b2\3\2\2\2/\u00b5\3\2\2\2\61\u00b8\3\2\2\2\63\u00bb\3\2\2"+
		"\2\65\u00c1\3\2\2\2\67\u00c4\3\2\2\29\u00c9\3\2\2\2;\u00ce\3\2\2\2=\u00d2"+
		"\3\2\2\2?\u00d4\3\2\2\2A\u00d6\3\2\2\2C\u00d8\3\2\2\2E\u00ea\3\2\2\2G"+
		"\u00ed\3\2\2\2I\u00f3\3\2\2\2K\u00fe\3\2\2\2M\u0111\3\2\2\2O^\7&\2\2P"+
		"Q\t\2\2\2Q_\4\62;\2RS\7u\2\2S]\7r\2\2TU\7h\2\2U]\7r\2\2VW\7j\2\2W]\7r"+
		"\2\2XY\7t\2\2Y]\7x\2\2Z[\7t\2\2[]\7c\2\2\\R\3\2\2\2\\T\3\2\2\2\\V\3\2"+
		"\2\2\\X\3\2\2\2\\Z\3\2\2\2]_\3\2\2\2^P\3\2\2\2^\\\3\2\2\2_\4\3\2\2\2`"+
		"a\7r\2\2ab\7w\2\2bc\7u\2\2cd\7j\2\2d\6\3\2\2\2ef\7c\2\2fg\7f\2\2gh\7f"+
		"\2\2hi\7t\2\2ij\7g\2\2jk\7u\2\2kl\7u\2\2l\b\3\2\2\2mn\7r\2\2no\7q\2\2"+
		"op\7r\2\2p\n\3\2\2\2qr\7c\2\2rs\7f\2\2st\7f\2\2t\f\3\2\2\2uv\7c\2\2vw"+
		"\7f\2\2wx\7f\2\2xy\7k\2\2y\16\3\2\2\2z{\7u\2\2{|\7w\2\2|}\7d\2\2}\20\3"+
		"\2\2\2~\177\7u\2\2\177\u0080\7w\2\2\u0080\u0081\7d\2\2\u0081\u0082\7k"+
		"\2\2\u0082\22\3\2\2\2\u0083\u0084\7o\2\2\u0084\u0085\7w\2\2\u0085\u0086"+
		"\7n\2\2\u0086\u0087\7v\2\2\u0087\24\3\2\2\2\u0088\u0089\7o\2\2\u0089\u008a"+
		"\7w\2\2\u008a\u008b\7n\2\2\u008b\u008c\7v\2\2\u008c\u008d\7k\2\2\u008d"+
		"\26\3\2\2\2\u008e\u008f\7f\2\2\u008f\u0090\7k\2\2\u0090\u0091\7x\2\2\u0091"+
		"\30\3\2\2\2\u0092\u0093\7f\2\2\u0093\u0094\7k\2\2\u0094\u0095\7x\2\2\u0095"+
		"\u0096\7k\2\2\u0096\32\3\2\2\2\u0097\u0098\7p\2\2\u0098\u0099\7q\2\2\u0099"+
		"\u009a\7v\2\2\u009a\34\3\2\2\2\u009b\u009c\7q\2\2\u009c\u009d\7t\2\2\u009d"+
		"\36\3\2\2\2\u009e\u009f\7u\2\2\u009f\u00a0\7y\2\2\u00a0 \3\2\2\2\u00a1"+
		"\u00a2\7n\2\2\u00a2\u00a3\7y\2\2\u00a3\"\3\2\2\2\u00a4\u00a5\7o\2\2\u00a5"+
		"\u00a6\7x\2\2\u00a6$\3\2\2\2\u00a7\u00a8\7d\2\2\u00a8&\3\2\2\2\u00a9\u00aa"+
		"\7d\2\2\u00aa\u00ab\7e\2\2\u00ab(\3\2\2\2\u00ac\u00ad\7n\2\2\u00ad\u00ae"+
		"\7g\2\2\u00ae*\3\2\2\2\u00af\u00b0\7n\2\2\u00b0\u00b1\7v\2\2\u00b1,\3"+
		"\2\2\2\u00b2\u00b3\7g\2\2\u00b3\u00b4\7s\2\2\u00b4.\3\2\2\2\u00b5\u00b6"+
		"\7i\2\2\u00b6\u00b7\7g\2\2\u00b7\60\3\2\2\2\u00b8\u00b9\7i\2\2\u00b9\u00ba"+
		"\7v\2\2\u00ba\62\3\2\2\2\u00bb\u00bc\7r\2\2\u00bc\u00bd\7t\2\2\u00bd\u00be"+
		"\7k\2\2\u00be\u00bf\7p\2\2\u00bf\u00c0\7v\2\2\u00c0\64\3\2\2\2\u00c1\u00c2"+
		"\7n\2\2\u00c2\u00c3\7k\2\2\u00c3\66\3\2\2\2\u00c4\u00c5\7j\2\2\u00c5\u00c6"+
		"\7c\2\2\u00c6\u00c7\7n\2\2\u00c7\u00c8\7v\2\2\u00c88\3\2\2\2\u00c9\u00ca"+
		"\7h\2\2\u00ca\u00cb\7t\2\2\u00cb\u00cc\7g\2\2\u00cc\u00cd\7g\2\2\u00cd"+
		":\3\2\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7g\2\2\u00d0\u00d1\7y\2\2\u00d1"+
		"<\3\2\2\2\u00d2\u00d3\7<\2\2\u00d3>\3\2\2\2\u00d4\u00d5\7*\2\2\u00d5@"+
		"\3\2\2\2\u00d6\u00d7\7+\2\2\u00d7B\3\2\2\2\u00d8\u00dc\t\3\2\2\u00d9\u00db"+
		"\t\4\2\2\u00da\u00d9\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc"+
		"\u00dd\3\2\2\2\u00ddD\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00eb\7\62\2\2"+
		"\u00e0\u00e2\7/\2\2\u00e1\u00e0\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e3"+
		"\3\2\2\2\u00e3\u00e7\4\63;\2\u00e4\u00e6\4\62;\2\u00e5\u00e4\3\2\2\2\u00e6"+
		"\u00e9\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00eb\3\2"+
		"\2\2\u00e9\u00e7\3\2\2\2\u00ea\u00df\3\2\2\2\u00ea\u00e1\3\2\2\2\u00eb"+
		"F\3\2\2\2\u00ec\u00ee\t\5\2\2\u00ed\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2"+
		"\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f2"+
		"\b$\2\2\u00f2H\3\2\2\2\u00f3\u00f4\7\61\2\2\u00f4\u00f5\7\61\2\2\u00f5"+
		"\u00f9\3\2\2\2\u00f6\u00f8\n\6\2\2\u00f7\u00f6\3\2\2\2\u00f8\u00fb\3\2"+
		"\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fc\3\2\2\2\u00fb"+
		"\u00f9\3\2\2\2\u00fc\u00fd\b%\2\2\u00fdJ\3\2\2\2\u00fe\u00ff\7\61\2\2"+
		"\u00ff\u0100\7,\2\2\u0100\u0109\3\2\2\2\u0101\u0108\n\7\2\2\u0102\u0103"+
		"\7\61\2\2\u0103\u0108\n\b\2\2\u0104\u0105\7,\2\2\u0105\u0108\n\t\2\2\u0106"+
		"\u0108\5K&\2\u0107\u0101\3\2\2\2\u0107\u0102\3\2\2\2\u0107\u0104\3\2\2"+
		"\2\u0107\u0106\3\2\2\2\u0108\u010b\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u010a"+
		"\3\2\2\2\u010a\u010c\3\2\2\2\u010b\u0109\3\2\2\2\u010c\u010d\7,\2\2\u010d"+
		"\u010e\7\61\2\2\u010e\u010f\3\2\2\2\u010f\u0110\b&\2\2\u0110L\3\2\2\2"+
		"\u0111\u0112\13\2\2\2\u0112\u0113\b\'\3\2\u0113\u0114\3\2\2\2\u0114\u0115"+
		"\b\'\2\2\u0115N\3\2\2\2\r\2\\^\u00dc\u00e1\u00e7\u00ea\u00ef\u00f9\u0107"+
		"\u0109\4\2\3\2\3\'\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}