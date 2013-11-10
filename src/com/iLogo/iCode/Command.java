package com.iLogo.iCode;

public abstract class Command {
	
    public Object accept(CmdVisitor v) {
    	System.out.println("CmdVisitor Not Found!");
    	return null;
    }
	
	public static class AssignCmd extends Command {
		
		public Variable receiver;
		public Variable op;
		
		public AssignCmd(Variable receiver, Variable op)
		{
			this.receiver = receiver;
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitAssignCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + op.toString();
		}
	}
	
	public static class AddCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public AddCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitAddCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " + " + op2.toString();
		}
	}
	
	public static class SubCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public SubCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitSubCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " - " + op2.toString();
		}
	}
	
	public static class MulCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public MulCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitMulCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " * " + op2.toString();
		}
	}
	
	public static class DivCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public DivCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitDivCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " / " + op2.toString();
		}
	}
	
	public static class ModCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public ModCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitModCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " % " + op2.toString();
		}
	}
	
	public static class NegCmd extends Command {
		
		public Variable receiver;
		public Variable op;
		
		public NegCmd(Variable receiver, Variable op)
		{
			this.receiver = receiver;
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitNegCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					"- " + op.toString();
		}
	}
	
	public static class EquCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public EquCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitEquCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " == " + op2.toString();
		}
	}

	public static class NeqCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public NeqCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitNeqCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " != " + op2.toString();
		}
	}
	
	public static class LesCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public LesCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitLesCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " < " + op2.toString();
		}
	}
	
	public static class LeqCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public LeqCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitLeqCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " <= " + op2.toString();
		}
	}
	
	public static class GtrCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public GtrCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitGtrCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " > " + op2.toString();
		}
	}
	
	public static class GeqCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public GeqCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitGeqCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " >= " + op2.toString();
		}
	}
	
	public static class LandCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public LandCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitLandCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " && " + op2.toString();
		}
	}
	
	public static class LorCmd extends Command {
		
		public Variable receiver;
		public Variable op1;
		public Variable op2;
		
		public LorCmd(Variable receiver, Variable op1, Variable op2)
		{
			this.receiver = receiver;
			this.op1 = op1;
			this.op2 = op2;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitLorCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + 
					op1.toString() + " || " + op2.toString();
		}
	}

	public static class LnotCmd extends Command {
		
		public Variable receiver;
		public Variable op;
		
		public LnotCmd(Variable receiver, Variable op)
		{
			this.receiver = receiver;
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitLnotCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = ! " + 
					op.toString();
		}
	}
	
	public static class BranchCmd extends Command {
		
		public Label lbl;
		
		public BranchCmd(Label lbl)
		{
			this.lbl = lbl;
		}
		
		@Override
		public Label accept(CmdVisitor v)
		{
			return v.visitBranchCmd(this);
		}
		
		public String toString()
		{
			return "Branch: " + lbl.toString();
		}
	}
	
	public static class BeqzCmd extends Command {
		
		public Label lbl;
		public Variable var;
		
		public BeqzCmd(Variable var, Label lbl)
		{
			this.var = var;
			this.lbl = lbl;
		}
		
		@Override
		public Label accept(CmdVisitor v)
		{
			return v.visitBeqzCmd(this);
		}
		
		public String toString()
		{
			return "Beqz: (" + var.toString() + ") " +  lbl.toString();
		}
	}
	
	public static class BnezCmd extends Command {
		
		public Label lbl;
		public Variable var;
		
		public BnezCmd(Variable var, Label lbl)
		{
			this.var = var;
			this.lbl = lbl;
		}
		
		@Override
		public Label accept(CmdVisitor v)
		{
			return v.visitBnezCmd(this);
		}
		
		public String toString()
		{
			return "Bnez: (" + var.toString() + ") " +  lbl.toString();
		}
	}
	
	public static class ReturnCmd extends Command {
		
		public Variable var;
		
		public ReturnCmd(Variable var)
		{
			this.var = var;
		}
		
		@Override
		public Integer accept(CmdVisitor v)
		{
			return v.visitReturnCmd(this);
		}
		
		public String toString()
		{
			if (var == null) return "Return";
			else return "Return: " + var.toString();
		}
	}
	
	public static class ParmCmd extends Command {
		
		public Variable var;
		
		public ParmCmd(Variable var)
		{
			this.var = var;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitParmCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "Parm: " + var.toString();
		}
	}
	
	public static class CallCmd extends Command {
		
		public Variable receiver;
		public Functy func;
		
		public CallCmd(Variable receiver, Functy func)
		{
			this.receiver = receiver;
			this.func = func;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitCallCmd(this);
			return null;
		}
		
		public String toString()
		{
			return receiver.toString() + " = " + "Call: " + func.toString();
		}
	}
	
	public static class DecCmd extends Command {
		
		public Variable op;
		
		public DecCmd(Variable op)
		{
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitDecCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "Dec : " + op.toString();
		}
	}
	
	public static class PaintForwardCmd extends Command {
		
		public Variable op;
		
		public PaintForwardCmd(Variable op)
		{
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitPaintForwardCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "PaintForward : " + op.toString();
		}
	}
	
	public static class TurnCmd extends Command {
		
		public Variable op;
		
		public TurnCmd(Variable op)
		{
			this.op = op;
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitTurnCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "Turn : " + op.toString();
		}
	}
	
	public static class PenUpCmd extends Command {
		
		public PenUpCmd() {
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitPenUpCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "PenUp";
		}
	}
	public static class PenDownCmd extends Command {
		
		public PenDownCmd() {
		}
		
		@Override
		public Object accept(CmdVisitor v)
		{
			v.visitPenDownCmd(this);
			return null;
		}
		
		public String toString()
		{
			return "PenDown";
		}
	}
	
	
	 /**
     * A generic visitor class for cmds.
     */
   public static abstract class CmdVisitor {

       public CmdVisitor() {
           super();
       }

       public void visitAssignCmd(AssignCmd that) {
           visitCmd(that);
       }
       
       public void visitAddCmd(AddCmd that) {
           visitCmd(that);
       }
       
       public void visitSubCmd(SubCmd that) {
           visitCmd(that);
       }
       
       public void visitMulCmd(MulCmd that) {
           visitCmd(that);
       }
       
       public void visitDivCmd(DivCmd that) {
           visitCmd(that);
       }
       
       public void visitModCmd(ModCmd that) {
           visitCmd(that);
       }
       
       public void visitEquCmd(EquCmd that) {
           visitCmd(that);
       }
       
       public void visitNegCmd(NegCmd that) {
           visitCmd(that);
       }
       
       public void visitNeqCmd(NeqCmd that) {
           visitCmd(that);
       }
       
       public void visitLesCmd(LesCmd that) {
           visitCmd(that);
       }
       
       public void visitLeqCmd(LeqCmd that) {
           visitCmd(that);
       }
       
       public void visitGtrCmd(GtrCmd that) {
           visitCmd(that);
       }
       
       public void visitGeqCmd(GeqCmd that) {
           visitCmd(that);
       }
       
       public void visitLandCmd(LandCmd that) {
           visitCmd(that);
       }
       
       public void visitLorCmd(LorCmd that) {
           visitCmd(that);
       }
       
       public void visitLnotCmd(LnotCmd that) {
           visitCmd(that);
       }
       
       public Label visitBranchCmd(BranchCmd that) {
    	   visitCmd(that);
    	   return null;
       }
       
       public Label visitBeqzCmd(BeqzCmd that) {
    	   visitCmd(that);
    	   return null;
       }
       
       public Label visitBnezCmd(BnezCmd that) {
    	   visitCmd(that);
    	   return null;
       }
       
       public Integer visitReturnCmd(ReturnCmd that) {
    	   visitCmd(that);
    	   return null;
       }
       
       public void visitParmCmd(ParmCmd that) {
    	   visitCmd(that);
       }
       
       public void visitCallCmd(CallCmd that) {
    	   visitCmd(that);
       }
       
       public void visitDecCmd(DecCmd that) {
    	   visitCmd(that);
       }
       
       public void visitPaintForwardCmd(PaintForwardCmd that) {
    	   visitCmd(that);
       }
       
       public void visitTurnCmd(TurnCmd that) {
    	   visitCmd(that);
       }
       
       public void visitPenUpCmd(PenUpCmd that) {
    	   visitCmd(that);
       }
       
       public void visitPenDownCmd(PenDownCmd that) {
    	   visitCmd(that);
       }
       
       
       public void visitCmd(Command that) {
           assert false;
       }
   }
   
}
