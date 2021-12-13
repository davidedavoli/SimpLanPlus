package ast;

import java.util.ArrayList;

import ast.node.*;
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
		ArrayList<Node> declarations = new ArrayList<>();
		ArrayList<Node> statements = new ArrayList<>();
		
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

		for (Node dc : declarations){
			((MetaNode) dc).setParent(res);
		}

		for (Node st : statements){
			((MetaNode) st).setParent(res);
		}

		return res;
	}
	
	@Override
	public Node visitStatement(StatementContext ctx){
		MetaNode tmp=null;
		if (ctx.assignment()!= null) {//WARNING Casting
			LhsNode l = (LhsNode)visit(ctx.assignment().lhs());
			ExpNode e = (ExpNode) visit(ctx.assignment().exp());
			tmp = new AssignmentNode(l, e);
			l.setParent(tmp);
			e.setParent(tmp);
		}
		else if (ctx.deletion()!= null) {
			tmp = (DeletionNode) visit(ctx.deletion());
		}
		else if (ctx.print()!= null) {
			tmp = (PrintNode) visit(ctx.print());
		}
		else if (ctx.ret()!= null) {
			tmp = (RetNode) visit(ctx.ret());
		}
		else if (ctx.ite()!= null) {
			tmp = (IfNode) visit(ctx.ite());
		}
		else if (ctx.call()!= null) {
			tmp = (CallNode) visit(ctx.call());
		}
		else if (ctx.block()!= null) {
			tmp = (BlockNode) visitBlock(ctx.block());
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
		FunNode res;
		IdNode id = new IdNode(ctx.ID().getText());
		if (ctx.type()!=null)
			res = new FunNode(ctx.ID().getText(), (TypeNode) visit(ctx.type()),id);//WARNING casting
		else
			res = new FunNode(ctx.ID().getText(), new VoidTypeNode(),id);//WARNING casting

		//add argument declarations
		//we are getting a shortcut here by constructing directly the ParNode
		//this could be done differently by visiting instead the VardecContext
		for(ArgContext arg : ctx.arg()){
			res.addPar( visitArg(arg));//WARNING casting
		}

		//add body
		BlockNode block = (BlockNode) visitBlock(ctx.block());

		block.setParent(res);
		for (Node arg: res.getPars()) {
			((MetaNode) arg).setParent(res);
		}

		//add the body and the inner declarations to the function
		res.addFunBlock( block);
		
		return res;		
	}
	
	@Override
	public Node visitDecVar(DecVarContext ctx) {
		//visit the type
		TypeNode typeNode = (TypeNode) visit(ctx.type());
		
		ExpNode expNode = null;
		//visit the exp
		if (ctx.exp()!=null) {
			 expNode = (ExpNode) visit(ctx.exp());
		}

		IdNode id = new IdNode(ctx.ID().getText());
		
		//build the varNode
		VarNode res = new VarNode(id, typeNode, expNode);
		id.setParent(res);
		if (expNode!=null)
			expNode.setParent(res);
		return res;
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
	public ArgNode visitArg(ArgContext ctx) {
		ArgNode res;
		TypeNode type = visitType(ctx.type());
		IdNode id = new IdNode(ctx.ID().getText());
		res = new ArgNode(id, type);
		id.setParent(res);
		return res;
	}
	
	@Override
	public Node visitAssignment(AssignmentContext ctx) {
		MetaNode res;

		LhsNode l = visitLhs(ctx.lhs());
		ExpNode e = (ExpNode) visit(ctx.exp());
		res = new AssignmentNode(l, e);

		l.setParent(res);
		e.setParent(res);

		return res;
	}
	
	@Override 
	public LhsNode visitLhs(LhsContext ctx) {
		LhsNode res;
		if (ctx.lhs()!=null) {
			LhsNode inner = visitLhs(ctx.lhs());
			res = new LhsNode(inner);
			inner.setParent(res);
		}
		else
			res = new IdNode(ctx.ID().getText());
		return res;
	}
	
	@Override
	public Node visitDeletion(DeletionContext ctx) {
		MetaNode res;

		IdExpNode id = new IdExpNode(ctx.ID().getText());
		res = new DeletionNode(id);
		id.setParent(res);

		return res;
	}
	
	@Override
	public Node visitPrint(PrintContext ctx) {
		ExpNode e = (ExpNode) visit(ctx.exp());
		PrintNode res = new PrintNode(e);
		e.setParent(res);
		return res;
	}
	
	@Override
	public Node visitRet(RetContext ctx) {

		MetaNode res;
		
		ParserRuleContext fct=ctx.getParent();
		while (fct !=null && !(fct instanceof DecFunContext))
			fct=fct.getParent();

		ExpNode e = (ExpNode) visit(ctx.exp());

		if (fct==null)
			res = new RetNode(e, null);//in caso in cui il ret non sia in una funzione
		else if (((DecFunContext)fct).type()!= null)
			res = new RetNode(e, (TypeNode)visit(((DecFunContext)fct).type())); //WARNING Double casting
		else
			res = new RetNode(e, new VoidTypeNode());

		if (e!=null)
			e.setParent(res);
		return res;
	}
	
	@Override
	public Node visitIte(IteContext ctx) {
		
		//create the resulting node
		IfNode res;
		
		//visit the conditional, then the then branch, and then the else branch
		//notice once again the need of named terminals in the rule, this is because
		//we need to point to the right expression among the 3 possible ones in the rule
		
		ExpNode condExp = (ExpNode) visit (ctx.exp());
		
		MetaNode thenExp = (MetaNode) visit (ctx.statement(0));
		MetaNode elseExp = null;
		if(ctx.statement(1) != null){
			elseExp = (MetaNode) visit (ctx.statement(1));
		}
		//build the @res properly and return it
		res = new IfNode( condExp, thenExp, elseExp);

		condExp.setParent(res);
		thenExp.setParent(res);
		if (elseExp!=null)
			elseExp.setParent(res);

		return res;
	}
	
	@Override
	public Node visitCall(CallContext ctx) {
		//this corresponds to a function invocation
		//declare the result
		MetaNode res;
		IdNode id = new IdNode(ctx.ID().getText());
		//get the invocation arguments
		ArrayList<ExpNode> args = new ArrayList<>();

		for(ExpContext exp : ctx.exp())
			args.add((ExpNode) visit(exp));

		//instantiate the invocation
		res = new CallNode(id, args);

		for(ExpNode exp : args)
			exp.setParent(res);

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
		ExpNode l = (ExpNode) visit(ctx.left);
		ExpNode r = (ExpNode) visit(ctx.right);
		MetaNode res = new BinExpNode(l, ctx.op.getText(), r);

		l.setParent(res);
		r.setParent(res);

		return res;
	}
	
	private LhsExpNode visitLhsExp(LhsContext ctx) {
		LhsExpNode res;
		if (ctx.ID() != null){
			return new IdExpNode(ctx.ID().getText());
		}
		else {
			LhsExpNode l = visitLhsExp(ctx.lhs());
			res = new LhsExpNode(l);
			l.setParent(res);
			return res;
		}
	}
	
	@Override
	public Node visitDerExp(DerExpContext ctx) {
		return visitLhsExp(ctx.lhs());
	}

	@Override
	public Node visitNewExp(NewExpContext ctx){
		if (ctx.getParent() instanceof AssignmentContext) {
			return new NewNode((TypeNode)visit(ctx.type()));
		}
		else if (ctx.getParent() instanceof DecVarContext)
			return new NewNode((TypeNode)visit(ctx.type()));
		else if (ctx.getParent() instanceof RetContext) {
			ParserRuleContext fct=ctx.getParent();
			while (fct !=null && !(fct instanceof DecFunContext))
				fct=fct.getParent();

			return new NewNode((TypeNode)visit(ctx.type()));
		}
		else return new NewNode(null);
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
		MetaNode res;
		Node inner = visit(ctx.call());
		//there is no need to perform a check here, the lexer ensures this text is a boolean
		res = new CallExpNode((CallNode) inner);
		((CallNode) inner).setParent(res);
		return res;
	}

	public Node visitMainBlock(BlockContext ctx, Boolean isMainBlock) {

		//resulting node of the right type
		BlockNode res;

		//list of declarations in @res
		ArrayList<Node> declarations = new ArrayList<>();
		ArrayList<Node> statements = new ArrayList<>();

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

		for (Node dc : declarations){
			((MetaNode) dc).setParent(res);
		}

		for (Node st : statements){
			((MetaNode) st).setParent(res);
		}

		return res;
	}
	@Override
	public Node visit(ParseTree tree){
		return tree != null ? super.visit(tree) : null;
	}

}
