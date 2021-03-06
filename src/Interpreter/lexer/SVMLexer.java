package Interpreter.lexer;

import java.util.HashMap;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REGISTER=1, PUSH=2, ADDRESS=3, POP=4, ADD=5, ADDI=6, SUB=7, SUBI=8, MULT=9, 
		MULTI=10, DIV=11, DIVI=12, NOT=13, OR=14, AND=15, STOREW=16, LOADW=17, 
		MOVE=18, BRANCH=19, BCOND=20, LE=21, LT=22, EQ=23, GE=24, GT=25, JAL=26, 
		JR=27, PRINT=28, LOAD=29, HALT=30, FREE=31, NEW=32, COL=33, LPAR=34, RPAR=35, 
		LABEL=36, NUMBER=37, WHITESP=38, LINECOMMENTS=39, BLOCKCOMMENTS=40, ERR=41;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"REGISTER", "PUSH", "ADDRESS", "POP", "ADD", "ADDI", "SUB", "SUBI", "MULT", 
			"MULTI", "DIV", "DIVI", "NOT", "OR", "AND", "STOREW", "LOADW", "MOVE", 
			"BRANCH", "BCOND", "LE", "LT", "EQ", "GE", "GT", "JAL", "JR", "PRINT", 
			"LOAD", "HALT", "FREE", "NEW", "COL", "LPAR", "RPAR", "LABEL", "NUMBER", 
			"WHITESP", "LINECOMMENTS", "BLOCKCOMMENTS", "ERR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'push'", "'address'", "'pop'", "'add'", "'addi'", "'sub'", 
			"'subi'", "'mult'", "'multi'", "'div'", "'divi'", "'not'", "'or'", "'and'", 
			"'sw'", "'lw'", "'mv'", "'b'", "'bc'", "'le'", "'lt'", "'eq'", "'ge'", 
			"'gt'", "'jal'", "'jr'", "'print'", "'li'", "'halt'", "'free'", "'new'", 
			"':'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "REGISTER", "PUSH", "ADDRESS", "POP", "ADD", "ADDI", "SUB", "SUBI", 
			"MULT", "MULTI", "DIV", "DIVI", "NOT", "OR", "AND", "STOREW", "LOADW", 
			"MOVE", "BRANCH", "BCOND", "LE", "LT", "EQ", "GE", "GT", "JAL", "JR", 
			"PRINT", "LOAD", "HALT", "FREE", "NEW", "COL", "LPAR", "RPAR", "LABEL", 
			"NUMBER", "WHITESP", "LINECOMMENTS", "BLOCKCOMMENTS", "ERR"
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
		case 40:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2+\u012a\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2f\n\2\5\2"+
		"h\n\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3"+
		"\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3"+
		"\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3!\3!"+
		"\3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\7%\u00ef\n%\f%\16%\u00f2\13%\3&\3&\5"+
		"&\u00f6\n&\3&\3&\7&\u00fa\n&\f&\16&\u00fd\13&\5&\u00ff\n&\3\'\6\'\u0102"+
		"\n\'\r\'\16\'\u0103\3\'\3\'\3(\3(\3(\3(\7(\u010c\n(\f(\16(\u010f\13(\3"+
		"(\3(\3)\3)\3)\3)\3)\3)\3)\3)\3)\7)\u011c\n)\f)\16)\u011f\13)\3)\3)\3)"+
		"\3)\3)\3*\3*\3*\3*\3*\2\2+\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32"+
		"\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+\3\2\n\4\2cctt\4\2"+
		"C\\c|\5\2\62;C\\c|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\4\2,,\61\61\3\2,,"+
		"\3\2\61\61\2\u0139\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E"+
		"\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2"+
		"\2\2\2S\3\2\2\2\3U\3\2\2\2\5i\3\2\2\2\7n\3\2\2\2\tv\3\2\2\2\13z\3\2\2"+
		"\2\r~\3\2\2\2\17\u0083\3\2\2\2\21\u0087\3\2\2\2\23\u008c\3\2\2\2\25\u0091"+
		"\3\2\2\2\27\u0097\3\2\2\2\31\u009b\3\2\2\2\33\u00a0\3\2\2\2\35\u00a4\3"+
		"\2\2\2\37\u00a7\3\2\2\2!\u00ab\3\2\2\2#\u00ae\3\2\2\2%\u00b1\3\2\2\2\'"+
		"\u00b4\3\2\2\2)\u00b6\3\2\2\2+\u00b9\3\2\2\2-\u00bc\3\2\2\2/\u00bf\3\2"+
		"\2\2\61\u00c2\3\2\2\2\63\u00c5\3\2\2\2\65\u00c8\3\2\2\2\67\u00cc\3\2\2"+
		"\29\u00cf\3\2\2\2;\u00d5\3\2\2\2=\u00d8\3\2\2\2?\u00dd\3\2\2\2A\u00e2"+
		"\3\2\2\2C\u00e6\3\2\2\2E\u00e8\3\2\2\2G\u00ea\3\2\2\2I\u00ec\3\2\2\2K"+
		"\u00fe\3\2\2\2M\u0101\3\2\2\2O\u0107\3\2\2\2Q\u0112\3\2\2\2S\u0125\3\2"+
		"\2\2Ug\7&\2\2VW\t\2\2\2Wh\4\62;\2XY\7u\2\2Yf\7r\2\2Z[\7h\2\2[f\7r\2\2"+
		"\\]\7j\2\2]f\7r\2\2^_\7d\2\2_`\7u\2\2`f\7r\2\2ab\7c\2\2bf\7n\2\2cd\7t"+
		"\2\2df\7c\2\2eX\3\2\2\2eZ\3\2\2\2e\\\3\2\2\2e^\3\2\2\2ea\3\2\2\2ec\3\2"+
		"\2\2fh\3\2\2\2gV\3\2\2\2ge\3\2\2\2h\4\3\2\2\2ij\7r\2\2jk\7w\2\2kl\7u\2"+
		"\2lm\7j\2\2m\6\3\2\2\2no\7c\2\2op\7f\2\2pq\7f\2\2qr\7t\2\2rs\7g\2\2st"+
		"\7u\2\2tu\7u\2\2u\b\3\2\2\2vw\7r\2\2wx\7q\2\2xy\7r\2\2y\n\3\2\2\2z{\7"+
		"c\2\2{|\7f\2\2|}\7f\2\2}\f\3\2\2\2~\177\7c\2\2\177\u0080\7f\2\2\u0080"+
		"\u0081\7f\2\2\u0081\u0082\7k\2\2\u0082\16\3\2\2\2\u0083\u0084\7u\2\2\u0084"+
		"\u0085\7w\2\2\u0085\u0086\7d\2\2\u0086\20\3\2\2\2\u0087\u0088\7u\2\2\u0088"+
		"\u0089\7w\2\2\u0089\u008a\7d\2\2\u008a\u008b\7k\2\2\u008b\22\3\2\2\2\u008c"+
		"\u008d\7o\2\2\u008d\u008e\7w\2\2\u008e\u008f\7n\2\2\u008f\u0090\7v\2\2"+
		"\u0090\24\3\2\2\2\u0091\u0092\7o\2\2\u0092\u0093\7w\2\2\u0093\u0094\7"+
		"n\2\2\u0094\u0095\7v\2\2\u0095\u0096\7k\2\2\u0096\26\3\2\2\2\u0097\u0098"+
		"\7f\2\2\u0098\u0099\7k\2\2\u0099\u009a\7x\2\2\u009a\30\3\2\2\2\u009b\u009c"+
		"\7f\2\2\u009c\u009d\7k\2\2\u009d\u009e\7x\2\2\u009e\u009f\7k\2\2\u009f"+
		"\32\3\2\2\2\u00a0\u00a1\7p\2\2\u00a1\u00a2\7q\2\2\u00a2\u00a3\7v\2\2\u00a3"+
		"\34\3\2\2\2\u00a4\u00a5\7q\2\2\u00a5\u00a6\7t\2\2\u00a6\36\3\2\2\2\u00a7"+
		"\u00a8\7c\2\2\u00a8\u00a9\7p\2\2\u00a9\u00aa\7f\2\2\u00aa \3\2\2\2\u00ab"+
		"\u00ac\7u\2\2\u00ac\u00ad\7y\2\2\u00ad\"\3\2\2\2\u00ae\u00af\7n\2\2\u00af"+
		"\u00b0\7y\2\2\u00b0$\3\2\2\2\u00b1\u00b2\7o\2\2\u00b2\u00b3\7x\2\2\u00b3"+
		"&\3\2\2\2\u00b4\u00b5\7d\2\2\u00b5(\3\2\2\2\u00b6\u00b7\7d\2\2\u00b7\u00b8"+
		"\7e\2\2\u00b8*\3\2\2\2\u00b9\u00ba\7n\2\2\u00ba\u00bb\7g\2\2\u00bb,\3"+
		"\2\2\2\u00bc\u00bd\7n\2\2\u00bd\u00be\7v\2\2\u00be.\3\2\2\2\u00bf\u00c0"+
		"\7g\2\2\u00c0\u00c1\7s\2\2\u00c1\60\3\2\2\2\u00c2\u00c3\7i\2\2\u00c3\u00c4"+
		"\7g\2\2\u00c4\62\3\2\2\2\u00c5\u00c6\7i\2\2\u00c6\u00c7\7v\2\2\u00c7\64"+
		"\3\2\2\2\u00c8\u00c9\7l\2\2\u00c9\u00ca\7c\2\2\u00ca\u00cb\7n\2\2\u00cb"+
		"\66\3\2\2\2\u00cc\u00cd\7l\2\2\u00cd\u00ce\7t\2\2\u00ce8\3\2\2\2\u00cf"+
		"\u00d0\7r\2\2\u00d0\u00d1\7t\2\2\u00d1\u00d2\7k\2\2\u00d2\u00d3\7p\2\2"+
		"\u00d3\u00d4\7v\2\2\u00d4:\3\2\2\2\u00d5\u00d6\7n\2\2\u00d6\u00d7\7k\2"+
		"\2\u00d7<\3\2\2\2\u00d8\u00d9\7j\2\2\u00d9\u00da\7c\2\2\u00da\u00db\7"+
		"n\2\2\u00db\u00dc\7v\2\2\u00dc>\3\2\2\2\u00dd\u00de\7h\2\2\u00de\u00df"+
		"\7t\2\2\u00df\u00e0\7g\2\2\u00e0\u00e1\7g\2\2\u00e1@\3\2\2\2\u00e2\u00e3"+
		"\7p\2\2\u00e3\u00e4\7g\2\2\u00e4\u00e5\7y\2\2\u00e5B\3\2\2\2\u00e6\u00e7"+
		"\7<\2\2\u00e7D\3\2\2\2\u00e8\u00e9\7*\2\2\u00e9F\3\2\2\2\u00ea\u00eb\7"+
		"+\2\2\u00ebH\3\2\2\2\u00ec\u00f0\t\3\2\2\u00ed\u00ef\t\4\2\2\u00ee\u00ed"+
		"\3\2\2\2\u00ef\u00f2\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1"+
		"J\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f3\u00ff\7\62\2\2\u00f4\u00f6\7/\2\2"+
		"\u00f5\u00f4\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\u00fb"+
		"\4\63;\2\u00f8\u00fa\4\62;\2\u00f9\u00f8\3\2\2\2\u00fa\u00fd\3\2\2\2\u00fb"+
		"\u00f9\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2"+
		"\2\2\u00fe\u00f3\3\2\2\2\u00fe\u00f5\3\2\2\2\u00ffL\3\2\2\2\u0100\u0102"+
		"\t\5\2\2\u0101\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\u0101\3\2\2\2\u0103"+
		"\u0104\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u0106\b\'\2\2\u0106N\3\2\2\2"+
		"\u0107\u0108\7\61\2\2\u0108\u0109\7\61\2\2\u0109\u010d\3\2\2\2\u010a\u010c"+
		"\n\6\2\2\u010b\u010a\3\2\2\2\u010c\u010f\3\2\2\2\u010d\u010b\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\u0110\3\2\2\2\u010f\u010d\3\2\2\2\u0110\u0111\b("+
		"\2\2\u0111P\3\2\2\2\u0112\u0113\7\61\2\2\u0113\u0114\7,\2\2\u0114\u011d"+
		"\3\2\2\2\u0115\u011c\n\7\2\2\u0116\u0117\7\61\2\2\u0117\u011c\n\b\2\2"+
		"\u0118\u0119\7,\2\2\u0119\u011c\n\t\2\2\u011a\u011c\5Q)\2\u011b\u0115"+
		"\3\2\2\2\u011b\u0116\3\2\2\2\u011b\u0118\3\2\2\2\u011b\u011a\3\2\2\2\u011c"+
		"\u011f\3\2\2\2\u011d\u011b\3\2\2\2\u011d\u011e\3\2\2\2\u011e\u0120\3\2"+
		"\2\2\u011f\u011d\3\2\2\2\u0120\u0121\7,\2\2\u0121\u0122\7\61\2\2\u0122"+
		"\u0123\3\2\2\2\u0123\u0124\b)\2\2\u0124R\3\2\2\2\u0125\u0126\13\2\2\2"+
		"\u0126\u0127\b*\3\2\u0127\u0128\3\2\2\2\u0128\u0129\b*\2\2\u0129T\3\2"+
		"\2\2\r\2eg\u00f0\u00f5\u00fb\u00fe\u0103\u010d\u011b\u011d\4\2\3\2\3*"+
		"\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}