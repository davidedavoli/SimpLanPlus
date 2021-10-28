package ast;

import java.util.ArrayList;

import ast.node.ArgNode;
import ast.node.IdNode;
import ast.node.LhsNode;
import ast.node.Node;
import ast.node.dec.FunNode;
import ast.node.exp.single_exp.NewNode;
import ast.node.dec.VarNode;
import ast.node.exp.*;
import ast.node.exp.single_exp.BoolNode;
import ast.node.exp.single_exp.IntNode;
import ast.node.exp.NotExpNode;
import ast.node.statements.*;
import parser.*;
import parser.SimpLanPlusParser.*;
import ast.node.types.BoolTypeNode;
import ast.node.types.IntTypeNode;
import ast.node.types.PointerTypeNode;
import ast.node.types.TypeNode;
import ast.node.types.VoidTypeNode;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class SimpLanPlusVisitorImpl extends SimpLanPlusBaseVisitor<Node> {
	
	
	@Override
	public Node visitBlock(BlockContext ctx) {
		
		//resulting node of the right type
		BlockNode res;

		//list of declarations in @res
		ArrayList<Node> declarations = new ArrayList<Node>();
		ArrayList<Node> statements = new ArrayList<Node>();
		
		//visit all nodes corresponding to declarations inside the let context and store them in @declarations
		//notice that the ctx.let().dec() method returns a list, this is because of the use of * or + in the grammar
		//antlr detects this is a group and therefore returns a list
		if (ctx.declaration()!=null) {
			for (DeclarationContext dc : ctx.declaration()){
					declarations.add( visit(dc) );
			}
		}
		
		if (ctx.statement()!= null) {
			for (StatementContext st : ctx.statement()){
				statements.add( visit(st) );
			}
		}
		
		//visit exp context
		
		//build @res accordingly with the result of the visits to its content
		res = new BlockNode(declarations,  statements,false);

		return res;
	}
	
	@Override
	public Node visitStatement(StatementContext ctx){
		Node tmp=null;
		if (ctx.assignment()!= null) {//WARNING Casting
			tmp = new AssignmentNode((LhsNode)visit(ctx.assignment().lhs()), (ExpNode) visit(ctx.assignment().exp()));
		}
		else if (ctx.deletion()!= null) {
			tmp = visit(ctx.deletion());
		}
		else if (ctx.print()!= null) {
			tmp = visit(ctx.print());
		}
		else if (ctx.ret()!= null) {
			tmp = visit(ctx.ret());
		}
		else if (ctx.ite()!= null) {
			tmp = visit(ctx.ite());
		}
		else if (ctx.call()!= null) {
			tmp = visit(ctx.call());
		}
		else if (ctx.block()!= null) {
			tmp = visitBlock(ctx.block());
		}
		return tmp;
	}

	/*
	@Override
	public Node visitDeclaration(DeclarationContext ctx) {
		if (ctx.decFun()!= null) {
			return visitDecFun(ctx.decFun());
		}
		if (ctx.decVar()!= null) {
			return visitDecVar(ctx.decVar());
		}
		return null;
	}
	*/
	
	@Override 
	public Node visitDecFun(DecFunContext ctx) {
		//initialize @res with the visits to the type and its ID
		FunNode res =null;
		//System.out.println("VISITOR "+ctx.ID().getText());
		if (ctx.type()!=null)
			res = new FunNode(ctx.ID().getText(), (TypeNode) visit(ctx.type()));//WARNING casting
		else
			res = new FunNode(ctx.ID().getText(), new VoidTypeNode());//WARNING casting

		//add argument declarations
		//we are getting a shortcut here by constructing directly the ParNode
		//this could be done differently by visiting instead the VardecContext
		for(ArgContext arg : ctx.arg())
			res.addPar( new ArgNode(arg.ID().getText(), (TypeNode)visit( arg.type() )) );//WARNING casting
		
		//add body
		BlockNode block = (BlockNode) visitBlock(ctx.block());
		
		//add the body and the inner declarations to the function
		res.addFunBlock( block);
		
		return res;		
	}
	
	@Override
	public Node visitDecVar(DecVarContext ctx) {
		//visit the type
		TypeNode typeNode = (TypeNode) visit(ctx.type());//WARING occhio al casting
		
		Node expNode = null;
		//visit the exp
		if (ctx.exp()!=null) {
			 expNode = visit(ctx.exp());
		}
		
		//build the varNode
		return new VarNode(new IdNode(ctx.ID().getText()), typeNode, expNode);
	}

	@Override
	public TypeNode visitType(TypeContext ctx) {
		
		if(ctx.getText().equals("int"))
			return new IntTypeNode();
		else if(ctx.getText().equals("bool"))
			return new BoolTypeNode();
		else if(ctx.type()!= null)
			return new PointerTypeNode((TypeNode) visit(ctx.type())); //WARNING Casting
		//this will never happen thanks to the parser
		return null;

	}
	
	@Override
	public Node visitArg(ArgContext ctx) {
		return new ArgNode(ctx.ID().getText(), (TypeNode)  visit(ctx.type() )); //WARNING CASTING
	}
	
	@Override
	public Node visitAssignment(AssignmentContext ctx) {
		return new AssignmentNode(visitLhs(ctx.lhs()), (ExpNode) visit(ctx.exp()));
	}
	
	@Override 
	public LhsNode visitLhs(LhsContext ctx) {
		if (ctx.lhs()!=null)
			return new LhsNode(visitLhs(ctx.lhs()));
		else
			return new IdNode(ctx.ID().getText());
	}
	
	@Override
	public Node visitDeletion(DeletionContext ctx) {
		return new DeletionNode(new IdNode(ctx.ID().getText()));
	}
	
	@Override
	public Node visitPrint(PrintContext ctx) {
		return new PrintNode(visit(ctx.exp()));
		//WARNING C'è il caso che non funizoni: nella grammatica SimpLan, la linea print era commentata
	}
	
	@Override
	public Node visitRet(RetContext ctx) {
		
		ParserRuleContext fct=ctx.getParent();
		while (fct !=null && !(fct instanceof DecFunContext))
			fct=fct.getParent();

		if (fct==null)
			return new RetNode(visit(ctx.exp()), null);//in caso in cui il ret non sia in una funzione
		else if (((DecFunContext)fct).type()!= null)
			return new RetNode(visit(ctx.exp()), (TypeNode)visit(((DecFunContext)fct).type())); //WARNING Double casting
		else
			return new RetNode(visit(ctx.exp()), new VoidTypeNode());
	}
	
	@Override
	public Node visitIte(IteContext ctx) {
		
		//create the resulting node
		IfNode res;
		
		//visit the conditional, then the then branch, and then the else branch
		//notice once again the need of named terminals in the rule, this is because
		//we need to point to the right expression among the 3 possible ones in the rule
		
		ExpNode condExp = (ExpNode) visit (ctx.exp());
		
		Node thenExp = visit (ctx.statement(0));
		Node elseExp = null;
		if(ctx.statement(1) != null){
			elseExp = visit (ctx.statement(1));
		}
		//build the @res properly and return it
		res = new IfNode( condExp, thenExp, elseExp);
		
		return res;
	}
	
	@Override
	public Node visitCall(CallContext ctx) {
		//this corresponds to a function invocation
		//declare the result
		Node res;

		//get the invocation arguments
		ArrayList<ExpNode> args = new ArrayList<ExpNode>();

		for(ExpContext exp : ctx.exp())
			args.add((ExpNode) visit(exp));

		//instantiate the invocation
		res = new CallNode(ctx.ID().getText(), args);

		return res;
	}
	
	@Override
	public Node visitBaseExp(BaseExpContext ctx) {
		
		//this is actually nothing in the sense that for the ast the parenthesis are not relevant
		//the thing is that the structure of the ast will ensure the operational order by giving
		//a larger depth (closer to the leafs) to those expressions with higher importance
		
		//this is actually the default implementation for this method in the SimpLanBaseVisitor class
		//therefore it can be safely removed here
		
		return visit (ctx.exp());

	}
	
	@Override
	public Node visitBinExp(BinExpContext ctx) {
		return new BinExpNode((ExpNode) visit(ctx.left), ctx.op.getText(), (ExpNode) visit(ctx.right));
	}
	
	private LhsExpNode visitLhsExp(LhsContext ctx) {
		if (ctx.ID() != null){
			return new IdExpNode(ctx.ID().getText());
		}
		else
			return new LhsExpNode(visitLhsExp(ctx.lhs()),visitLhs(ctx.lhs()));
	}
	
	@Override
	public Node visitDerExp(DerExpContext ctx) {
		return visitLhsExp(ctx.lhs());
	}
	
//	@Override
//	public Node visitNewExp(NewExpContext ctx){ 
//		if (ctx.getParent() instanceof AssignmentContext) {
//			NewNode tmp = new NewNode(null);
//			tmp.setID(visitLhsExp(((AssignmentContext)ctx.getParent()).lhs()).getID());
//			return tmp;//usiamo la visitLhsExp dal momento che non ha side-effects sulla ST in modo da poter usare getID()
//		}
//		else if (ctx.getParent() instanceof DecVarContext)
//			return new NewNode((TypeNode)visit(((DecVarContext)(ctx.getParent())).type()));
//		else if (ctx.getParent() instanceof RetContext) {
//			ParserRuleContext fct=ctx.getParent();
//			while (fct !=null && !(fct instanceof DecFunContext))
//				fct=fct.getParent();
//			//in questo caso non ha senso chiedersi se siamo nel corpo di una funzione perché ne siamo certi dal momento che il medesimo check è stato fatto nel ramo return
//			return new NewNode((((DecFunContext)fct).type()==null)?new VoidTypeNode(): (TypeNode) visit(((DecFunContext)fct).type()));
//		}
//		else return new NewNode(null); //WARNING in questo modo stiamo ammettendo solo uso di new nella forma type? id=new o return new.
//	}

	@Override
	public Node visitNewExp(NewExpContext ctx){
		if (ctx.getParent() instanceof AssignmentContext) {
			NewNode tmp = new NewNode((TypeNode)visit(ctx.type()));
//			tmp.setID(visitLhsExp(((AssignmentContext)ctx.getParent()).lhs()).getID());
			return tmp;//usiamo la visitLhsExp dal momento che non ha side-effects sulla ST in modo da poter usare getID()
		}
		else if (ctx.getParent() instanceof DecVarContext)
			return new NewNode((TypeNode)visit(ctx.type()));
		else if (ctx.getParent() instanceof RetContext) {
			ParserRuleContext fct=ctx.getParent();
			while (fct !=null && !(fct instanceof DecFunContext))
				fct=fct.getParent();
			//in questo caso non ha senso chiedersi se siamo nel corpo di una funzione perché ne siamo certi dal momento che il medesimo check è stato fatto nel ramo return
			return new NewNode((TypeNode)visit(ctx.type()));
		}
		else return new NewNode(null); //WARNING in questo modo stiamo ammettendo solo uso di new nella forma type? id=new o return new.
	}

	@Override public ExpNode visitValExp(SimpLanPlusParser.ValExpContext ctx) {
		return new IntNode(Integer.parseInt(ctx.NUMBER().getText()));
		}
	
	@Override public ExpNode visitNotExp(SimpLanPlusParser.NotExpContext ctx) {
		return new NotExpNode((ExpNode) visit(ctx.exp()));
		}
	
	@Override public ExpNode visitNegExp(SimpLanPlusParser.NegExpContext ctx) {
		return new NegExpNode((ExpNode) visit(ctx.exp()));
	}
	
	@Override
	public Node visitBoolExp(BoolExpContext ctx) {
		
		//there is no need to perform a check here, the lexer ensures this text is a boolean
		return new BoolNode(Boolean.parseBoolean(ctx.getText()));
	}
	
	@Override
	public Node visitCallExp(CallExpContext ctx) {
		Node inner = visit(ctx.call());
		//there is no need to perform a check here, the lexer ensures this text is a boolean
		return new CallExpNode((CallNode) inner);
	}

	public Node visitMainBlock(BlockContext ctx, Boolean isMainBlock) {

		//resulting node of the right type
		BlockNode res;

		//list of declarations in @res
		ArrayList<Node> declarations = new ArrayList<Node>();
		ArrayList<Node> statements = new ArrayList<Node>();

		//visit all nodes corresponding to declarations inside the let context and store them in @declarations
		//notice that the ctx.let().dec() method returns a list, this is because of the use of * or + in the grammar
		//antlr detects this is a group and therefore returns a list
		if (ctx.declaration()!=null) {
			for (DeclarationContext dc : ctx.declaration()){
				declarations.add( visit(dc) );
			}
		}

		if (ctx.statement()!= null) {
			for (StatementContext st : ctx.statement()){
				statements.add( visit(st) );
			}
		}

		//visit exp context

		//build @res accordingly with the result of the visits to its content
		res = new BlockNode(declarations,  statements,isMainBlock);

		return res;
	}
	@Override
	public Node visit(ParseTree tree){
		return tree != null ? super.visit(tree) : null;
	}

}
