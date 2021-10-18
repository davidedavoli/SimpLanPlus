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
		MULTI=10, DIV=11, DIVI=12, NOT=13, OR=14, AND=15, STOREW=16, LOADW=17, 
		MOVE=18, BRANCH=19, BCOND=20, LE=21, LT=22, EQ=23, GE=24, GT=25, JAL=26, 
		JR=27, PRINT=28, LOAD=29, HALT=30, FREE=31, NEW=32, COL=33, LPAR=34, RPAR=35, 
		LABEL=36, NUMBER=37, WHITESP=38, LINECOMMENTS=39, BLOCKCOMMENTS=40, ERR=41;
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
			"'subi'", "'mult'", "'multi'", "'div'", "'divi'", "'not'", "'or'", "'and'", 
			"'sw'", "'lw'", "'mv'", "'b'", "'bc'", "'le'", "'lt'", "'eq'", "'ge'", 
			"'gt'", "'jal'", "'jr'", "'print'", "'li'", "'halt'", "'free'", "'new'", 
			"':'", "'('", "')'"
		};
	}
	public static final String[] _LITERAL_NAMES = makeLiteralNames();
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
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PUSH) | (1L << ADDRESS) | (1L << POP) | (1L << ADD) | (1L << ADDI) | (1L << SUB) | (1L << SUBI) | (1L << MULT) | (1L << MULTI) | (1L << DIV) | (1L << DIVI) | (1L << NOT) | (1L << OR) | (1L << AND) | (1L << STOREW) | (1L << LOADW) | (1L << MOVE) | (1L << BRANCH) | (1L << BCOND) | (1L << LE) | (1L << LT) | (1L << EQ) | (1L << GE) | (1L << GT) | (1L << JAL) | (1L << JR) | (1L << PRINT) | (1L << LOAD) | (1L << HALT) | (1L << FREE) | (1L << NEW) | (1L << LABEL))) != 0)) {
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
		public TerminalNode AND() { return getToken(SVMParser.AND, 0); }
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
		public TerminalNode JAL() { return getToken(SVMParser.JAL, 0); }
		public TerminalNode JR() { return getToken(SVMParser.JR, 0); }
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
			setState(118);
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
				match(AND);
				setState(54);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(55);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(56);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 15:
				{
				setState(57);
				match(NOT);
				setState(58);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(59);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 16:
				{
				setState(60);
				match(STOREW);
				setState(61);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(62);
				((InstructionContext)_localctx).o = match(NUMBER);
				setState(63);
				match(LPAR);
				setState(64);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(65);
				match(RPAR);
				}
				break;
			case 17:
				{
				setState(66);
				match(LOADW);
				setState(67);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(68);
				((InstructionContext)_localctx).o = match(NUMBER);
				setState(69);
				match(LPAR);
				setState(70);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(71);
				match(RPAR);
				}
				break;
			case 18:
				{
				setState(72);
				match(LOAD);
				setState(73);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(74);
				((InstructionContext)_localctx).n = match(NUMBER);
				}
				break;
			case 19:
				{
				setState(75);
				match(MOVE);
				setState(76);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(77);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				}
				break;
			case 20:
				{
				setState(78);
				match(BRANCH);
				setState(79);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 21:
				{
				setState(80);
				match(BCOND);
				setState(81);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(82);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 22:
				{
				setState(83);
				match(EQ);
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
				match(LE);
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
				match(LT);
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
				match(GT);
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
				match(GE);
				setState(100);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				setState(101);
				((InstructionContext)_localctx).r2 = match(REGISTER);
				setState(102);
				((InstructionContext)_localctx).r3 = match(REGISTER);
				}
				break;
			case 27:
				{
				setState(103);
				match(JAL);
				setState(104);
				((InstructionContext)_localctx).l = match(LABEL);
				}
				break;
			case 28:
				{
				setState(105);
				match(JR);
				setState(106);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 29:
				{
				setState(107);
				match(FREE);
				setState(108);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 30:
				{
				setState(109);
				match(NEW);
				setState(110);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 31:
				{
				setState(111);
				match(PRINT);
				setState(112);
				((InstructionContext)_localctx).r1 = match(REGISTER);
				}
				break;
			case 32:
				{
				setState(113);
				match(PRINT);
				}
				break;
			case 33:
				{
				setState(114);
				match(HALT);
				}
				break;
			case 34:
				{
				setState(115);
				match(ADDRESS);
				}
				break;
			case 35:
				{
				setState(116);
				((InstructionContext)_localctx).l = match(LABEL);
				setState(117);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3+{\4\2\t\2\4\3\t\3"+
		"\3\2\7\2\b\n\2\f\2\16\2\13\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3y\n\3\3\3\2\2\4\2\4\2"+
		"\2\2\u009b\2\t\3\2\2\2\4x\3\2\2\2\6\b\5\4\3\2\7\6\3\2\2\2\b\13\3\2\2\2"+
		"\t\7\3\2\2\2\t\n\3\2\2\2\n\3\3\2\2\2\13\t\3\2\2\2\f\r\7\4\2\2\ry\7\'\2"+
		"\2\16\17\7\4\2\2\17y\7\3\2\2\20y\7\6\2\2\21\22\7\6\2\2\22y\7\3\2\2\23"+
		"\24\7\7\2\2\24\25\7\3\2\2\25\26\7\3\2\2\26y\7\3\2\2\27\30\7\b\2\2\30\31"+
		"\7\3\2\2\31\32\7\3\2\2\32y\7\'\2\2\33\34\7\t\2\2\34\35\7\3\2\2\35\36\7"+
		"\3\2\2\36y\7\3\2\2\37 \7\n\2\2 !\7\3\2\2!\"\7\3\2\2\"y\7\'\2\2#$\7\13"+
		"\2\2$%\7\3\2\2%&\7\3\2\2&y\7\3\2\2\'(\7\f\2\2()\7\3\2\2)*\7\3\2\2*y\7"+
		"\'\2\2+,\7\r\2\2,-\7\3\2\2-.\7\3\2\2.y\7\3\2\2/\60\7\16\2\2\60\61\7\3"+
		"\2\2\61\62\7\3\2\2\62y\7\'\2\2\63\64\7\20\2\2\64\65\7\3\2\2\65\66\7\3"+
		"\2\2\66y\7\3\2\2\678\7\21\2\289\7\3\2\29:\7\3\2\2:y\7\3\2\2;<\7\17\2\2"+
		"<=\7\3\2\2=y\7\3\2\2>?\7\22\2\2?@\7\3\2\2@A\7\'\2\2AB\7$\2\2BC\7\3\2\2"+
		"Cy\7%\2\2DE\7\23\2\2EF\7\3\2\2FG\7\'\2\2GH\7$\2\2HI\7\3\2\2Iy\7%\2\2J"+
		"K\7\37\2\2KL\7\3\2\2Ly\7\'\2\2MN\7\24\2\2NO\7\3\2\2Oy\7\3\2\2PQ\7\25\2"+
		"\2Qy\7&\2\2RS\7\26\2\2ST\7\3\2\2Ty\7&\2\2UV\7\31\2\2VW\7\3\2\2WX\7\3\2"+
		"\2Xy\7\3\2\2YZ\7\27\2\2Z[\7\3\2\2[\\\7\3\2\2\\y\7\3\2\2]^\7\30\2\2^_\7"+
		"\3\2\2_`\7\3\2\2`y\7\3\2\2ab\7\33\2\2bc\7\3\2\2cd\7\3\2\2dy\7\3\2\2ef"+
		"\7\32\2\2fg\7\3\2\2gh\7\3\2\2hy\7\3\2\2ij\7\34\2\2jy\7&\2\2kl\7\35\2\2"+
		"ly\7\3\2\2mn\7!\2\2ny\7\3\2\2op\7\"\2\2py\7\3\2\2qr\7\36\2\2ry\7\3\2\2"+
		"sy\7\36\2\2ty\7 \2\2uy\7\5\2\2vw\7&\2\2wy\7#\2\2x\f\3\2\2\2x\16\3\2\2"+
		"\2x\20\3\2\2\2x\21\3\2\2\2x\23\3\2\2\2x\27\3\2\2\2x\33\3\2\2\2x\37\3\2"+
		"\2\2x#\3\2\2\2x\'\3\2\2\2x+\3\2\2\2x/\3\2\2\2x\63\3\2\2\2x\67\3\2\2\2"+
		"x;\3\2\2\2x>\3\2\2\2xD\3\2\2\2xJ\3\2\2\2xM\3\2\2\2xP\3\2\2\2xR\3\2\2\2"+
		"xU\3\2\2\2xY\3\2\2\2x]\3\2\2\2xa\3\2\2\2xe\3\2\2\2xi\3\2\2\2xk\3\2\2\2"+
		"xm\3\2\2\2xo\3\2\2\2xq\3\2\2\2xs\3\2\2\2xt\3\2\2\2xu\3\2\2\2xv\3\2\2\2"+
		"y\5\3\2\2\2\4\tx";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}