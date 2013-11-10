package com.iLogo.draw;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.iLogo.iCode.Command.AddCmd;
import com.iLogo.iCode.Command.AssignCmd;
import com.iLogo.iCode.Command.BeqzCmd;
import com.iLogo.iCode.Command.BnezCmd;
import com.iLogo.iCode.Command.BranchCmd;
import com.iLogo.iCode.Command.CallCmd;
import com.iLogo.iCode.Command.CmdVisitor;
import com.iLogo.iCode.Command.DecCmd;
import com.iLogo.iCode.Command.DivCmd;
import com.iLogo.iCode.Command.EquCmd;
import com.iLogo.iCode.Command.GeqCmd;
import com.iLogo.iCode.Command.GtrCmd;
import com.iLogo.iCode.Command.LandCmd;
import com.iLogo.iCode.Command.LeqCmd;
import com.iLogo.iCode.Command.LesCmd;
import com.iLogo.iCode.Command.LnotCmd;
import com.iLogo.iCode.Command.LorCmd;
import com.iLogo.iCode.Command.ModCmd;
import com.iLogo.iCode.Command.MulCmd;
import com.iLogo.iCode.Command.NegCmd;
import com.iLogo.iCode.Command.NeqCmd;
import com.iLogo.iCode.Command.PaintForwardCmd;
import com.iLogo.iCode.Command.ParmCmd;
import com.iLogo.iCode.Command.PenDownCmd;
import com.iLogo.iCode.Command.PenUpCmd;
import com.iLogo.iCode.Command.ReturnCmd;
import com.iLogo.iCode.Command.SubCmd;
import com.iLogo.iCode.Command.TurnCmd;
import com.iLogo.iCode.Functy;
import com.iLogo.iCode.Label;
import com.iLogo.iCode.Variable;
import com.iLogo.iCode.Constant;

public class Drawer extends View {
	
	Paint paint;
	public ArrayList<Functy> functys;
	
	double currentX;
	double currentY;
	double oriX;
	double oriY;
	boolean penOn;
	ArrayList<Line> lines = new ArrayList<Line>();
	
	public Drawer(Context context) {
		super(context);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		setBackgroundColor(Color.WHITE);
		paint.setColor(Color.BLUE);
	}
	
	public void setFunctys(ArrayList<Functy> functys) {
		this.functys = functys;
		lines.clear();
		currentX = 225.0;
		currentY = 225.0;
		oriX = 0.0;
		oriY = 1.0;
		penOn = true;
	}
	
	public class FunctyInterpreter{
		
		int regSize;
		int reg[];
		Functy functy;
		
		public FunctyInterpreter(Functy functy, int[] actuals, int actualSize) {
			this.functy = functy;
			regSize = functy.variables.size();
			reg = new int[regSize];
			
			for (int i = 0; i < actualSize; i++)
				reg[i] = actuals[i];
			
			for (int i = actualSize; i < regSize; i++)
				if (functy.variables.get(i).isConstant())
					reg[i] = ((Constant)functy.variables.get(i)).number;
				else
					reg[i] = 0;
		}
		
	    public int visitFuncty() {
	        
			int currentCmd = 0, cmdSize = functy.commands.size();
			CommandInterpreter cmdItr = new Drawer.CommandInterpreter(reg);
			
			
			while (currentCmd < cmdSize) {
				
				Object ret;
				ret = functy.commands.get(currentCmd).accept(cmdItr);
				
				if (ret == null) currentCmd++;
				else
				{
					if (ret.getClass().getName().compareTo("java.lang.Integer") == 0)
						return ((Integer)ret).intValue();
					else
					{
						currentCmd = ((Label)ret).pos;
						Log.i("b",String.valueOf(currentCmd));
					}
				}
			}
			return 0;
	    }	
	}

	
	public class CommandInterpreter extends CmdVisitor {
		
		int reg[];
		ArrayList<Variable> actuals;
		
		public CommandInterpreter(int reg[]) {
			this.reg = reg;
			actuals = new ArrayList<Variable>();
		}

		@Override
		public void visitAssignCmd(AssignCmd assign) {
			
			reg[assign.receiver.tag] =  reg[assign.op.tag];
			
		}

		@Override
		public void visitAddCmd(AddCmd add) {
			
			reg[add.receiver.tag] = reg[add.op1.tag] + reg[add.op2.tag];
			
		}

		@Override
		public void visitSubCmd(SubCmd sub) {
			
			reg[sub.receiver.tag] = reg[sub.op1.tag] - reg[sub.op2.tag];
			
		}

		@Override
		public void visitMulCmd(MulCmd mul) {
			
			reg[mul.receiver.tag] = reg[mul.op1.tag] * reg[mul.op2.tag];
			
		}

		@Override
		public void visitDivCmd(DivCmd div) {
			
			reg[div.receiver.tag] = reg[div.op1.tag] / reg[div.op2.tag];
			
		}

		@Override
		public void visitModCmd(ModCmd mod) {
			
			reg[mod.receiver.tag] = reg[mod.op1.tag] % reg[mod.op2.tag];
			
		}

		@Override
		public void visitEquCmd(EquCmd equ) {
			
			reg[equ.receiver.tag] = reg[equ.op1.tag] == reg[equ.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitNegCmd(NegCmd neg) {
			
			reg[neg.receiver.tag] = -reg[neg.op.tag];
			
		}

		@Override
		public void visitNeqCmd(NeqCmd neq) {
			
			reg[neq.receiver.tag] = reg[neq.op1.tag] != reg[neq.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitLesCmd(LesCmd les) {
			
			reg[les.receiver.tag] = reg[les.op1.tag] < reg[les.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitLeqCmd(LeqCmd leq) {
			
			reg[leq.receiver.tag] = reg[leq.op1.tag] <= reg[leq.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitGtrCmd(GtrCmd gtr) {
			
			reg[gtr.receiver.tag] = reg[gtr.op1.tag] > reg[gtr.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitGeqCmd(GeqCmd geq) {
			
			reg[geq.receiver.tag] = reg[geq.op1.tag] >= reg[geq.op2.tag] ? 1 : 0;
			
		}

		@Override
		public void visitLandCmd(LandCmd land) {
			
			if (reg[land.op1.tag] == 1 && reg[land.op2.tag] == 1)
				reg[land.receiver.tag] = 1;
			else
				reg[land.receiver.tag] = 0;
		}

		@Override
		public void visitLorCmd(LorCmd lor) {
			
			if (reg[lor.op1.tag] == 1 || reg[lor.op2.tag] == 1)
				reg[lor.receiver.tag] = 1;
			else
				reg[lor.receiver.tag] = 0;
			
		}

		@Override
		public void visitLnotCmd(LnotCmd lnot) {
			
			reg[lnot.op.tag] = 1 - reg[lnot.op.tag]; 
			
		}

		@Override
		public Label visitBranchCmd(BranchCmd branch) {
			
			return branch.lbl;
			
		}

		@Override
		public Label visitBeqzCmd(BeqzCmd beqz) {
			
			if (reg[beqz.var.tag] == 0)
				return beqz.lbl;
			else
				return null;
			
		}

		@Override
		public Label visitBnezCmd(BnezCmd bnez) {
			
			if (reg[bnez.var.tag] != 0)
				return bnez.lbl;
			else
				return null;
			
		}

		@Override
		public Integer visitReturnCmd(ReturnCmd ret) {
			
			if (ret.var == null) return new Integer(0);
			return new Integer(reg[ret.var.tag]);
			
		}

		@Override
		public void visitParmCmd(ParmCmd parm) {
			
			actuals.add(parm.var);
			
		}

		@Override
		public void visitCallCmd(CallCmd call) {
			
			int a[] = new int[actuals.size()];
			for (int i = 0; i < actuals.size(); i++) 
				a[i] = reg[actuals.get(i).tag];
			FunctyInterpreter fi = new 
					FunctyInterpreter(call.func, a, actuals.size());
			actuals.clear();
			if (call.receiver != null)
				reg[call.receiver.tag] = fi.visitFuncty();
			else fi.visitFuncty();
			
		}

		@Override
		public void visitDecCmd(DecCmd dec) {
			reg[dec.op.tag]--;
		}

		@Override
		public void visitPaintForwardCmd(PaintForwardCmd pf) {
			double newX = currentX + oriX * reg[pf.op.tag];
			double newY = currentY + oriY * reg[pf.op.tag];
			if (penOn)
				lines.add(new Line(currentX, currentY, newX, newY));
			currentX = newX;
			currentY = newY;
			invalidate();
		}

		@Override
		public void visitTurnCmd(TurnCmd tr) {
			double cosValue = Math.cos((reg[tr.op.tag] * Math.PI) / 180 );
			double sinValue = Math.sin((reg[tr.op.tag] * Math.PI) / 180 );
			double tx = oriX;
			oriX = oriX * cosValue - oriY * sinValue;
			oriY = tx * sinValue + oriY * cosValue;
		}

		@Override
		public void visitPenUpCmd(PenUpCmd that) {
			penOn = false;
		}

		@Override
		public void visitPenDownCmd(PenDownCmd that) {
			penOn = true;
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Line line : lines) {
			canvas.drawLine(line.x0, line.y0, line.x1, line.y1, paint);
		}
	}
	
	public void clearDraw() {
		lines.clear();
		invalidate();
	}
}
