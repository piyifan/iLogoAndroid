package com.iLogo.typecheck;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.iLogo.driver.Driver;
import com.iLogo.driver.Location;
import com.iLogo.tree.Tree;
import com.iLogo.tree.Tree.Expr;
import com.iLogo.tree.Tree.FuncDef;
import com.iLogo.tree.Tree.VarDef;
import com.iLogo.type.*;

import com.iLogo.error.*;
import com.iLogo.frontend.Parser;

public class TypeCheck extends Tree.Visitor {

	private Stack<Tree> breaks = new Stack<Tree>();

	private FuncDef currentFunc;

	public static void checkType(Tree.TopLevel tree) {
		new TypeCheck().visitTopLevel(tree);
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.type = checkBinaryOp(expr.left, expr.right, expr.tag, expr.loc);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		if(expr.tag == Tree.NEG){
			if (expr.expr.type.equal(BaseType.ERROR)
					|| expr.expr.type.equal(BaseType.INT)) {
				expr.type = expr.expr.type;
			} else {
				issueError(new IncompatUnOpError(expr.getLocation(), "-",
						expr.expr.type.toString()));
				expr.type = BaseType.ERROR;
			}
		}
		else{
			if (!(expr.expr.type.equal(BaseType.BOOL) || expr.expr.type
					.equal(BaseType.ERROR))) {
				issueError(new IncompatUnOpError(expr.getLocation(), "!",
						expr.expr.type.toString()));
			}
			expr.type = BaseType.BOOL;			
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.type = BaseType.INT;
			break;
		case Tree.BOOL:
			literal.type = BaseType.BOOL;
			break;
		}
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (!Tree.funcs.containsKey(callExpr.func))
		{
			issueError(new FuncNotFoundError(callExpr.loc, callExpr.func));
			callExpr.type = BaseType.ERROR;
			return;
		}
		FuncDef f = Tree.funcs.get(callExpr.func);
		if (callExpr.actuals.size() != f.formals.size())
		{
			issueError(new BadArgCountError(callExpr.loc, callExpr.func,
					f.formals.size(), callExpr.actuals.size()) );
			callExpr.type = BaseType.ERROR;
			return;
		}
		for (Expr e : callExpr.actuals)
			e.accept(this);
		Iterator<VarDef> iter1 = f.formals.iterator();
		Iterator<Tree.Expr> iter2 = callExpr.actuals.iterator();
		for (int i = 1; iter1.hasNext(); i++) {
			Type t1 = BaseType.getType(iter1.next().type);
			Tree.Expr e = iter2.next();
			Type t2 = e.type;
			if (!t2.equal(BaseType.ERROR) && !t2.compatible(t1)) {
				issueError(new BadArgTypeError(e.getLocation(), i, 
						t2.toString(), t1.toString()));
			}
		}
		callExpr.type = BaseType.getType(f.returnType);
	}

	@Override
	public void visitExec(Tree.Exec exec){
		exec.expr.accept(this);
	}
	
	@Override
	public void visitIdent(Tree.Ident ident) {
		if (!currentFunc.vars.containsKey(ident.name))
		{
			issueError(new UndeclVarError(ident.loc, ident.name));
			ident.type = BaseType.ERROR;
		}
		else
			ident.type = BaseType.getType(currentFunc.vars.get(ident.name).type);
	}

	@Override
	public void visitFuncDef(Tree.FuncDef func) {
		this.currentFunc = func;
		func.body.accept(this);
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		if (!Tree.funcs.containsKey("main")) {
			issueError(new NoMainError(null));
		}
		for (Map.Entry<String, FuncDef> f : Tree.funcs.entrySet()) {
			f.getValue().accept(this);
		}
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		if (!assign.left.type.equal(BaseType.ERROR)
				&&!assign.expr.type.compatible(assign.left.type)) {
			issueError(new IncompatBinOpError(assign.getLocation(),
					assign.left.type.toString(), "=", assign.expr.type
							.toString()));
		}
	}

	@Override
	public void visitBreak(Tree.Break breakStmt) {
		if (breaks.empty()) {
			issueError(new BreakOutOfLoopError(breakStmt.getLocation()));
		}
	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		checkTestExpr(forLoop.condition);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		breaks.add(forLoop);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		breaks.pop();
	}
	
	@Override 
	public void visitRepeatLoop(Tree.RepeatLoop repeatLoop)
	{
		repeatLoop.times.accept(this);
		if (repeatLoop.times.type != BaseType.ERROR && 
				repeatLoop.times.type != BaseType.INT)
			issueError(new BadIntExpr(repeatLoop.times.loc));
		breaks.add(repeatLoop);
		if (repeatLoop.loopBody != null)
			repeatLoop.loopBody.accept(this);
		breaks.pop();
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		checkTestExpr(ifStmt.condition);
		if (ifStmt.trueBranch != null) {
			ifStmt.trueBranch.accept(this);
		}
		if (ifStmt.falseBranch != null) {
			ifStmt.falseBranch.accept(this);
		}
	}
	
	

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		Type returnType = BaseType.getType(currentFunc.returnType);
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
		}
		if (returnType.equal(BaseType.VOID)) {
			if (returnStmt.expr != null) {
				issueError(new BadReturnTypeError(returnStmt.getLocation(),
						returnType.toString(), returnStmt.expr.type.toString()));
			}
		} else if (returnStmt.expr == null) {
			issueError(new BadReturnTypeError(returnStmt.getLocation(),
					returnType.toString(), "void"));
		} else if (!returnStmt.expr.type.equal(BaseType.ERROR)
				&& !returnStmt.expr.type.compatible(returnType)) {
			issueError(new BadReturnTypeError(returnStmt.getLocation(),
					returnType.toString(), returnStmt.expr.type.toString()));
		}
	}
	
	@Override
	public void visitPaintForward(Tree.PaintForward pf) {
		pf.expr.accept(this);
		if (pf.expr.type != BaseType.ERROR && 
				pf.expr.type != BaseType.INT)
		{
			issueError(new BadIntExpr(pf.expr.loc));
			return;
		}
	}
	
	@Override
	public void visitTurn(Tree.Turn tr) {
		tr.expr.accept(this);
		if (tr.expr.type != BaseType.ERROR && 
				tr.expr.type != BaseType.INT)
		{
			issueError(new BadIntExpr(tr.expr.loc));
			return;
		}
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		checkTestExpr(whileLoop.condition);
		breaks.add(whileLoop);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	// visiting types
	@Override
	public void visitTypeIdent(Tree.TypeIdent type) {
		switch (type.typeTag) {
		case Tree.VOID:
			type.type = BaseType.VOID;
			break;
		case Tree.INT:
			type.type = BaseType.INT;
			break;
		case Tree.BOOL:
			type.type = BaseType.BOOL;
			break;
		}
	}

	private void issueError(BaseError error) {
		Driver.getDriver().issueError(error);
	}

	private Type checkBinaryOp(Tree.Expr left, Tree.Expr right, int op, Location location) {
		left.accept(this);
		right.accept(this);

		if (left.type.equal(BaseType.ERROR) || right.type.equal(BaseType.ERROR)) {
			switch (op) {
			case Tree.PLUS:
			case Tree.MINUS:
			case Tree.MUL:
			case Tree.DIV:
				return left.type;
			case Tree.MOD:
				return BaseType.INT;
			default:
				return BaseType.BOOL;
			}
		}

		boolean compatible = false;
		Type returnType = BaseType.ERROR;
		switch (op) {
		case Tree.PLUS:
		case Tree.MINUS:
		case Tree.MUL:
		case Tree.DIV:
			compatible = left.type.equals(BaseType.INT)
					&& left.type.equal(right.type);
			returnType = left.type;
			break;
		case Tree.GT:
		case Tree.GE:
		case Tree.LT:
		case Tree.LE:
			compatible = left.type.equal(BaseType.INT)
					&& left.type.equal(right.type);
			returnType = BaseType.BOOL;
			break;
		case Tree.MOD:
			compatible = left.type.equal(BaseType.INT)
					&& right.type.equal(BaseType.INT);
			returnType = BaseType.INT;
			break;
		case Tree.EQ:
		case Tree.NE:
			compatible = left.type.compatible(right.type)
					|| right.type.compatible(left.type);
			returnType = BaseType.BOOL;
			break;
		case Tree.AND:
		case Tree.OR:
			compatible = left.type.equal(BaseType.BOOL)
					&& right.type.equal(BaseType.BOOL);
			returnType = BaseType.BOOL;
			break;
		default:
			break;
		}

		if (!compatible) {
			issueError(new IncompatBinOpError(location, left.type.toString(),
					Parser.opStr(op), right.type.toString()));
		}
		return returnType;
	}

	private void checkTestExpr(Tree.Expr expr) {
		expr.accept(this);
		if (!expr.type.equal(BaseType.ERROR) && !expr.type.equal(BaseType.BOOL)) {
			issueError(new BadTestExpr(expr.getLocation()));
		}
	}

}
