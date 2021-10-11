// Generated from D:/universita/SimpLanPlus/src/Interpreter/lexer\SVM.g4 by ANTLR 4.9.1
package Interpreter.parser;

import Interpreter.ast.SVMListener;
import Interpreter.ast.SVMVisitor;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMParser extends Parser {
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
	public static final int
		RULE_assembly = 0, RULE_instruction = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"assembly", "instruction"
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

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SVMParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AssemblyContext extends ParserRuleContext {
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener) ((SVMListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).exitAssembly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor) return ((SVMVisitor<? extends T>)visitor).visitAssembly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PUSH) | (1L << ADDRESS) | (1L << POP) | (1L << ADD) | (1L << ADDI) | (1L << SUB) | (1L << SUBI) | (1L << MULT) | (1L << MULTI) | (1L << DIV) | (1L << DIVI) | (1L << NOT) | (1L << OR) | (1L << STOREW) | (1L << LOADW) | (1L << MOVE) | (1L << BRANCH) | (1L << BCOND) | (1L << LE) | (1L << LT) | (1L << EQ) | (1L << GE) | (1L << GT) | (1L << PRINT) | (1L << LOAD) | (1L << HALT) | (1L << FREE) | (1L << NEW) | (1L << LABEL))) != 0)) {
				{
				{
				setState(4);
				instruction();
				}
				}
				setState(9);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public Token n;
		public Token r1;
		public Token r2;
		public Token r3;
		public Token o;
		public Token l;
		public TerminalNode PUSH() { return getToken(SVMParser.PUSH, 0); }
		public TerminalNode POP() { return getToken(SVMParser.POP, 0); }
		public TerminalNode ADD() { return getToken(SVMParser.ADD, 0); }
		public TerminalNode ADDI() { return getToken(SVMParser.ADDI, 0); }
		public TerminalNode SUB() { return getToken(SVMParser.SUB, 0); }
		public TerminalNode SUBI() { return getToken(SVMParser.SUBI, 0); }
		public TerminalNode MULT() { return getToken(SVMParser.MULT, 0); }
		public TerminalNode MULTI() { return getToken(SVMParser.MULTI, 0); }
		public TerminalNode DIV() { return getToken(SVMParser.DIV, 0); }
		public TerminalNode DIVI() { return getToken(SVMParser.DIVI, 0); }
		public TerminalNode OR() { return getToken(SVMParser.OR, 0); }
		public TerminalNode NOT() { return getToken(SVMParser.NOT, 0); }
		public TerminalNode STOREW() { return getToken(SVMParser.STOREW, 0); }
		public TerminalNode LPAR() { return getToken(SVMParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(SVMParser.RPAR, 0); }
		public TerminalNode LOADW() { return getToken(SVMParser.LOADW, 0); }
		public TerminalNode LOAD() { return getToken(SVMParser.LOAD, 0); }
		public TerminalNode MOVE() { return getToken(SVMParser.MOVE, 0); }
		public TerminalNode BRANCH() { return getToken(SVMParser.BRANCH, 0); }
		public TerminalNode BCOND() { return getToken(SVMParser.BCOND, 0); }
		public TerminalNode EQ() { return getToken(SVMParser.EQ, 0); }
		public TerminalNode LE() { return getToken(SVMParser.LE, 0); }
		public TerminalNode LT() { return getToken(SVMParser.LT, 0); }
		public TerminalNode GT() { return getToken(SVMParser.GT, 0); }
		public TerminalNode GE() { return getToken(SVMParser.GE, 0); }
		public TerminalNode FREE() { return getToken(SVMParser.FREE, 0); }
		public TerminalNode NEW() { return getToken(SVMParser.NEW, 0); }
		public TerminalNode PRINT() { return getToken(SVMParser.PRINT, 0); }
		public TerminalNode HALT() { return getToken(SVMParser.HALT, 0); }
		public TerminalNode ADDRESS() { return getToken(SVMParser.ADDRESS, 0); }
		public TerminalNode COL() { return getToken(SVMParser.COL, 0); }
		public TerminalNode NUMBER() { return getToken(SVMParser.NUMBER, 0); }
		public List<TerminalNode> REGISTER() { return getTokens(SVMParser.REGISTER); }
		public TerminalNode REGISTER(int i) {
			return getToken(SVMParser.REGISTER, i);
		}
		public TerminalNode LABEL() { return getToken(SVMParser.LABEL, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SVMListener ) ((SVMListener)listener).exitInstruction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SVMVisitor ) return ((SVMVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(10);
				match(PUSH);
				setState(11);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 2:
				{
				setState(12);
				match(PUSH);
				setState(13);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 3:
				{
				setState(14);
				match(POP);
				}
				break;
			case 4:
				{
				setState(15);
				match(POP);
				setState(16);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 5:
				{
				setState(17);
				match(ADD);
				setState(18);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(19);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(20);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 6:
				{
				setState(21);
				match(ADDI);
				setState(22);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(23);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(24);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 7:
				{
				setState(25);
				match(SUB);
				setState(26);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(27);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(28);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 8:
				{
				setState(29);
				match(SUBI);
				setState(30);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(31);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(32);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 9:
				{
				setState(33);
				match(MULT);
				setState(34);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(35);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(36);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 10:
				{
				setState(37);
				match(MULTI);
				setState(38);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(39);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(40);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 11:
				{
				setState(41);
				match(DIV);
				setState(42);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(43);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(44);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 12:
				{
				setState(45);
				match(DIVI);
				setState(46);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(47);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(48);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 13:
				{
				setState(49);
				match(OR);
				setState(50);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(51);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(52);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 14:
				{
				setState(53);
				match(NOT);
				setState(54);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(55);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 15:
				{
				setState(56);
				match(STOREW);
				setState(57);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(58);
				((InstructionContext)_localctx).o = match(NUMBER);
				setState(59);
				match(LPAR);
				setState(60);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(61);
				match(RPAR);
				}
				break;
			case 16:
				{
				setState(62);
				match(LOADW);
				setState(63);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(64);
				((InstructionContext)_localctx).o = match(NUMBER);
				setState(65);
				match(LPAR);
				setState(66);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(67);
				match(RPAR);
				}
				break;
			case 17:
				{
				setState(68);
				match(LOAD);
				setState(69);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(70);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 18:
				{
				setState(71);
				match(MOVE);
				setState(72);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(73);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 19:
				{
				setState(74);
				match(BRANCH);
				setState(75);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 20:
				{
				setState(76);
				match(BCOND);
				setState(77);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(78);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 21:
				{
				setState(79);
				match(EQ);
				setState(80);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(81);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(82);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 22:
				{
				setState(83);
				match(LE);
				setState(84);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(85);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(86);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 23:
				{
				setState(87);
				match(LT);
				setState(88);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(89);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(90);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 24:
				{
				setState(91);
				match(GT);
				setState(92);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(93);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(94);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 25:
				{
				setState(95);
				match(GE);
				setState(96);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(97);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(98);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 26:
				{
				setState(99);
				match(FREE);
				setState(100);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 27:
				{
				setState(101);
				match(NEW);
				setState(102);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 28:
				{
				setState(103);
				match(PRINT);
				setState(104);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 29:
				{
				setState(105);
				match(PRINT);
				}
				break;
			case 30:
				{
				setState(106);
				match(HALT);
				}
				break;
			case 31:
				{
				setState(107);
				match(ADDRESS);
				}
				break;
			case 32:
				{
				setState(108);
				((InstructionContext)_localctx).l = match(LABEL);
				setState(109);
				match(COL);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3(s\4\2\t\2\4\3\t\3"+
		"\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\5\3q\n\3\3\3\2\2\4\2\4\2\2\2\u0090\2\t\3\2\2\2\4p\3\2\2\2"+
		"\6\b\5\4\3\2\7\6\3\2\2\2\b\13\3\2\2\2\t\7\3\2\2\2\t\n\3\2\2\2\n\3\3\2"+
		"\2\2\13\t\3\2\2\2\f\r\7\4\2\2\rq\7$\2\2\16\17\7\4\2\2\17q\7\3\2\2\20q"+
		"\7\6\2\2\21\22\7\6\2\2\22q\7\3\2\2\23\24\7\7\2\2\24\25\7\3\2\2\25\26\7"+
		"\3\2\2\26q\7\3\2\2\27\30\7\b\2\2\30\31\7\3\2\2\31\32\7\3\2\2\32q\7$\2"+
		"\2\33\34\7\t\2\2\34\35\7\3\2\2\35\36\7\3\2\2\36q\7\3\2\2\37 \7\n\2\2 "+
		"!\7\3\2\2!\"\7\3\2\2\"q\7$\2\2#$\7\13\2\2$%\7\3\2\2%&\7\3\2\2&q\7\3\2"+
		"\2\'(\7\f\2\2()\7\3\2\2)*\7\3\2\2*q\7$\2\2+,\7\r\2\2,-\7\3\2\2-.\7\3\2"+
		"\2.q\7\3\2\2/\60\7\16\2\2\60\61\7\3\2\2\61\62\7\3\2\2\62q\7$\2\2\63\64"+
		"\7\20\2\2\64\65\7\3\2\2\65\66\7\3\2\2\66q\7\3\2\2\678\7\17\2\289\7\3\2"+
		"\29q\7\3\2\2:;\7\21\2\2;<\7\3\2\2<=\7$\2\2=>\7!\2\2>?\7\3\2\2?q\7\"\2"+
		"\2@A\7\22\2\2AB\7\3\2\2BC\7$\2\2CD\7!\2\2DE\7\3\2\2Eq\7\"\2\2FG\7\34\2"+
		"\2GH\7\3\2\2Hq\7$\2\2IJ\7\23\2\2JK\7\3\2\2Kq\7\3\2\2LM\7\24\2\2Mq\7#\2"+
		"\2NO\7\25\2\2OP\7\3\2\2Pq\7#\2\2QR\7\30\2\2RS\7\3\2\2ST\7\3\2\2Tq\7\3"+
		"\2\2UV\7\26\2\2VW\7\3\2\2WX\7\3\2\2Xq\7\3\2\2YZ\7\27\2\2Z[\7\3\2\2[\\"+
		"\7\3\2\2\\q\7\3\2\2]^\7\32\2\2^_\7\3\2\2_`\7\3\2\2`q\7\3\2\2ab\7\31\2"+
		"\2bc\7\3\2\2cd\7\3\2\2dq\7\3\2\2ef\7\36\2\2fq\7\3\2\2gh\7\37\2\2hq\7\3"+
		"\2\2ij\7\33\2\2jq\7\3\2\2kq\7\33\2\2lq\7\35\2\2mq\7\5\2\2no\7#\2\2oq\7"+
		" \2\2p\f\3\2\2\2p\16\3\2\2\2p\20\3\2\2\2p\21\3\2\2\2p\23\3\2\2\2p\27\3"+
		"\2\2\2p\33\3\2\2\2p\37\3\2\2\2p#\3\2\2\2p\'\3\2\2\2p+\3\2\2\2p/\3\2\2"+
		"\2p\63\3\2\2\2p\67\3\2\2\2p:\3\2\2\2p@\3\2\2\2pF\3\2\2\2pI\3\2\2\2pL\3"+
		"\2\2\2pN\3\2\2\2pQ\3\2\2\2pU\3\2\2\2pY\3\2\2\2p]\3\2\2\2pa\3\2\2\2pe\3"+
		"\2\2\2pg\3\2\2\2pi\3\2\2\2pk\3\2\2\2pl\3\2\2\2pm\3\2\2\2pn\3\2\2\2q\5"+
		"\3\2\2\2\4\tp";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}