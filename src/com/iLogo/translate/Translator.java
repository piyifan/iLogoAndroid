package com.iLogo.translate;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import com.iLogo.iCode.*;
import com.iLogo.tree.Tree;
import com.iLogo.tree.Tree.VarDef;
import com.iLogo.tree.Tree.*;

public class Translator extends Tree.Visitor {
	
	ArrayList<Command> commands;
	ArrayList<Variable> variables;
	Map<String, VarDef> varList;
	ArrayList<Functy> functys;
	
	private Stack<Label> loopExits = new Stack<Label>();

	
	Functy currentFuncty;
	
	public ArrayList<Functy> returnFunctys() {
		return functys;
	}

	
	public void Mark(Label lbl) {
		lbl.setPos(commands.size());
	}
	
	public Variable createVariable() {
		Variable var = new Variable(variables.size());
		variables.add(var);
		return var;
	}

	@Override
	public void visitTopLevel(TopLevel topLevel) {
		
		functys = new ArrayList<Functy>();
		
		Tree.FuncDef mainFunc = Tree.funcs.get("main");
		currentFuncty = new Functy(0);
		mainFunc.functy = currentFuncty;
		functys.add(currentFuncty);
		
		for (Map.Entry<String, FuncDef> f : Tree.funcs.entrySet()) {
			if (f.getKey().compareTo("main") == 0 ) continue;
			currentFuncty = new Functy(functys.size());
			f.getValue().functy = currentFuncty;
			functys.add(currentFuncty);
		}
		
		for (Map.Entry<String, FuncDef> f : Tree.funcs.entrySet()) {
			currentFuncty = f.getValue().functy;
			commands = currentFuncty.commands;
			variables = currentFuncty.variables;
			varList = f.getValue().vars;
			f.getValue().accept(this);
		}
		
	}

	@Override
	public void visitFuncDef(FuncDef func) {
		
		//Add formals
		for (VarDef v : func.formals) {
			v.variable = new Variable(variables.size());
			variables.add(v.variable);
		}
		
		//Add variables
		for (Map.Entry<String, VarDef> v : func.vars.entrySet()) {
			if (v.getValue().variable != null) continue;
			v.getValue().variable = new Variable(variables.size());
			variables.add(v.getValue().variable);
		}
		
		//Scan body
		func.body.accept(this);
		
	}


	@Override
	public void visitBlock(Block block) {
		
		for (Tree s : block.block) {
			s.accept(this);
		}
		
	}

	@Override
	public void visitRepeatLoop(RepeatLoop repeatLoop) {
		repeatLoop.times.accept(this);
		
		Variable v = createVariable();
		
		Label loop = new Label();
		Label exit = new Label();
		commands.add(new Command.AssignCmd(v, repeatLoop.times.variable));
		Mark(loop);
		commands.add(new Command.BeqzCmd(v, exit));
		loopExits.push(exit);
		if (repeatLoop.loopBody != null) {
			repeatLoop.loopBody.accept(this);
		}
		commands.add(new Command.DecCmd(v));
		commands.add(new Command.BranchCmd(loop));
		loopExits.pop();
		Mark(exit);
	}

	@Override
	public void visitWhileLoop(WhileLoop whileLoop) {
		Label loop = new Label();
		Mark(loop);
		whileLoop.condition.accept(this);
		Label exit = new Label();
		commands.add(new Command.BeqzCmd(whileLoop.condition.variable, exit));
		loopExits.push(exit);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		commands.add(new Command.BranchCmd(loop));
		loopExits.pop();
		Mark(exit);
	}

	@Override
	public void visitForLoop(ForLoop forLoop) {
		
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = new Label();
		Label loop = new Label();
		commands.add(new Command.BranchCmd(cond));
		Mark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		Mark(cond);
		forLoop.condition.accept(this);
		Label exit = new Label();
		commands.add(new Command.BeqzCmd(forLoop.condition.variable, exit));
		loopExits.push(exit);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		commands.add(new Command.BranchCmd(loop));
		loopExits.pop();
		Mark(exit);
		
	}

	@Override
	public void visitIf(If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = new Label();
			commands.add(new Command.BeqzCmd(ifStmt.condition.variable, falseLabel));
			ifStmt.trueBranch.accept(this);
			Label exit = new Label();
			commands.add(new Command.BranchCmd(exit));
			Mark(falseLabel);
			ifStmt.falseBranch.accept(this);
			Mark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = new Label();
			commands.add(new Command.BeqzCmd(ifStmt.condition.variable, exit));
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
		    Mark(exit);
		}
	}

	@Override
	public void visitBreak(Break breakStmt) {
		commands.add(new Command.BranchCmd(loopExits.peek()));
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			commands.add(new Command.ReturnCmd(returnStmt.expr.variable));
		} else {
			commands.add(new Command.ReturnCmd(null));
		}
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		commands.add(new Command.AssignCmd(assign.left.variable, assign.expr.variable));
	}

	@Override
	public void visitUnary(Unary expr) {
		expr.expr.accept(this);
		expr.variable = createVariable();
		switch (expr.tag){
		case Tree.NEG:
			commands.add(new Command.NegCmd(expr.variable, expr.expr.variable));
			break;
		default:
			commands.add(new Command.LnotCmd(expr.variable, expr.expr.variable));
		}
	}

	@Override
	public void visitBinary(Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		expr.variable = createVariable();
		switch (expr.tag) {
		case Tree.PLUS:
			commands.add(new 
			  Command.AddCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.MINUS:
			commands.add(new 
			  Command.SubCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.MUL:
			commands.add(new 
			  Command.MulCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.DIV:
			commands.add(new 
		      Command.DivCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.MOD:
			commands.add(new 
			  Command.ModCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.AND:
			commands.add(new 
			    Command.LandCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.OR:
			commands.add(new 
				Command.LorCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.LT:
			commands.add(new 
			    Command.LesCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.LE:
			commands.add(new 
			    Command.LeqCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.GT:
			commands.add(new 
			    Command.GtrCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.GE:
			commands.add(new 
			    Command.GeqCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.EQ:
			commands.add(new 
			    Command.EquCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		case Tree.NE:
			commands.add(new 
			    Command.NeqCmd(expr.variable, expr.left.variable, expr.right.variable));
			break;
		}
	}
	
	@Override
	public void visitExec(Exec expr) {
		expr.expr.accept(this);
	}

	@Override
	public void visitCallExpr(CallExpr callExpr) {
		for (Tree.Expr expr : callExpr.actuals) {
			expr.accept(this);
		}
		for (Tree.Expr expr : callExpr.actuals) {
			commands.add(new Command.ParmCmd(expr.variable));
		}
		callExpr.variable = createVariable();
		commands.add(new Command.CallCmd(callExpr.variable, 
				Tree.funcs.get(callExpr.func).functy));
	}

	@Override
	public void visitIdent(Ident ident) {
		ident.variable = varList.get(ident.name).variable;
	}

	@Override
	public void visitLiteral(Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.variable = new Constant(variables.size(), ((Integer)literal.value).intValue() );
			variables.add(literal.variable);
			break;
		case Tree.BOOL:
			literal.variable = new Constant(variables.size(), (Boolean)(literal.value) ? 1 : 0 );
			variables.add(literal.variable);
			break;
		}
	}

	@Override
	public void visitPaintForward(Tree.PaintForward pf) {
		pf.expr.accept(this);
		commands.add(new Command.PaintForwardCmd(pf.expr.variable));
	}

	@Override
	public void visitTurn(Tree.Turn tr) {
		tr.expr.accept(this);
		commands.add(new Command.TurnCmd(tr.expr.variable));
	}

	@Override
	public void visitPenUp(Tree.PenUp that) {
		commands.add(new Command.PenUpCmd());
	}

	@Override
	public void visitPenDown(Tree.PenDown that) {
		commands.add(new Command.PenDownCmd());
	}
	
	public void printTo() {
		System.out.println("#Functy = " + String.valueOf(functys.size()) );
		System.out.println();
		for (Functy ft : functys) {
			System.out.println( ft.toString() + " {");
			System.out.println( "#Variable = " + String.valueOf(ft.variables.size()));
			int i=0;
			for (Command cmd : ft.commands) 
			{
				System.out.println(String.valueOf(i) + ": " + cmd.toString());
				i++;
			}
			System.out.println("}");
			System.out.println();
		}
	}

}
