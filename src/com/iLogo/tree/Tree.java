package com.iLogo.tree;

import java.util.List;
import java.util.Map;

import com.iLogo.driver.*;
import com.iLogo.iCode.Functy;
import com.iLogo.iCode.Variable;
import com.iLogo.type.Type;
import com.iLogo.utils.*;

import com.iLogo.error.DeclConflictError;


/**
 * Root class for abstract syntax tree nodes. It provides
 *  definitions for specific tree nodes as subclasses nested inside
 *  There are 40 such subclasses.
 *
 *  Each subclass is highly standardized.  It generally contains only tree
 *  fields for the syntactic subcomponents of the node.  Some classes that
 *  represent identifier uses or definitions also define a
 *  Symbol field that denotes the represented identifier.  Classes
 *  for non-local jumps also carry the jump target as a field.  The root
 *  class Tree itself defines fields for the tree's type and
 *  position.  No other fields are kept in a tree node; instead parameters
 *  are passed to methods accessing the node.
 *
 *  The only method defined in subclasses is `visit' which applies a
 *  given visitor to the tree. The actual tree processing is done by
 *  visitor classes in other packages. The abstract class
 *  Visitor, as well as an Factory interface for trees, are
 *  defined as inner classes in Tree.
 *  @see TreeMaker
 *  @see TreeInfo
 *  @see TreeTranslator
 *  @see Pretty
 */
public abstract class Tree {

    /**
     * Toplevel nodes, of type TopLevel, representing entire source files.
     */
    public static final int TOPLEVEL = 1;

    /**
     * Import clauses, of type Import.
     */
    public static final int IMPORT = TOPLEVEL + 1;

    /**
     * Class definitions, of type ClassDef.
     */
    public static final int CLASSDEF = IMPORT + 1;

    /**
     * Method definitions, of type MethodDef.
     */
    public static final int METHODDEF = CLASSDEF + 1;

    /**
     * Variable definitions, of type VarDef.
     */
    public static final int VARDEF = METHODDEF + 1;

    /**
     * The no-op statement ";", of type Skip
     */
    public static final int SKIP = VARDEF + 1;

    /**
     * Blocks, of type Block.
     */
    public static final int BLOCK = SKIP + 1;

    /**
     * Do-while loops, of type DoLoop.
     */
    public static final int DOLOOP = BLOCK + 1;

    /**
     * While-loops, of type WhileLoop.
     */
    public static final int WHILELOOP = DOLOOP + 1;

    /**
     * For-loops, of type ForLoop.
     */
    public static final int FORLOOP = WHILELOOP + 1;

    /**
     * Labelled statements, of type Labelled.
     */
    public static final int LABELLED = FORLOOP + 1;

    /**
     * Switch statements, of type Switch.
     */
    public static final int SWITCH = LABELLED + 1;

    /**
     * Case parts in switch statements, of type Case.
     */
    public static final int CASE = SWITCH + 1;

    /**
     * Synchronized statements, of type Synchonized.
     */
    public static final int SYNCHRONIZED = CASE + 1;

    /**
     * Try statements, of type Try.
     */
    public static final int TRY = SYNCHRONIZED + 1;

    /**
     * Catch clauses in try statements, of type Catch.
     */
    public static final int CATCH = TRY + 1;

    /**
     * Conditional expressions, of type Conditional.
     */
    public static final int CONDEXPR = CATCH + 1;

    /**
     * Conditional statements, of type If.
     */
    public static final int IF = CONDEXPR + 1;

    /**
     * Expression statements, of type Exec.
     */
    public static final int EXEC = IF + 1;

    /**
     * Break statements, of type Break.
     */
    public static final int BREAK = EXEC + 1;

    /**
     * Continue statements, of type Continue.
     */
    public static final int CONTINUE = BREAK + 1;

    /**
     * Return statements, of type Return.
     */
    public static final int RETURN = CONTINUE + 1;

    /**
     * Throw statements, of type Throw.
     */
    public static final int THROW = RETURN + 1;

    /**
     * Assert statements, of type Assert.
     */
    public static final int ASSERT = THROW + 1;

    /**
     * Method invocation expressions, of type Apply.
     */
    public static final int APPLY = ASSERT + 1;

    /**
     * Class instance creation expressions, of type NewClass.
     */
    public static final int NEWCLASS = APPLY + 1;

    /**
     * Array creation expressions, of type NewArray.
     */
    public static final int NEWARRAY = NEWCLASS + 1;

    /**
     * Parenthesized subexpressions, of type Parens.
     */
    public static final int PARENS = NEWARRAY + 1;

    /**
     * Assignment expressions, of type Assign.
     */
    public static final int ASSIGN = PARENS + 1;

    /**
     * Type cast expressions, of type TypeCast.
     */
    public static final int TYPECAST = ASSIGN + 1;

    /**
     * Type test expressions, of type TypeTest.
     */
    public static final int TYPETEST = TYPECAST + 1;

    /**
     * Indexed array expressions, of type Indexed.
     */
    public static final int INDEXED = TYPETEST + 1;

    /**
     * Selections, of type Select.
     */
    public static final int SELECT = INDEXED + 1;

    /**
     * Simple identifiers, of type Ident.
     */
    public static final int IDENT = SELECT + 1;

    /**
     * Literals, of type Literal.
     */
    public static final int LITERAL = IDENT + 1;

    /**
     * Basic type identifiers, of type TypeIdent.
     */
    public static final int TYPEIDENT = LITERAL + 1;

    /**
     * Class types, of type TypeClass.
     */    
    public static final int TYPECLASS = TYPEIDENT + 1;

    /**
     * Array types, of type TypeArray.
     */
    public static final int TYPEARRAY = TYPECLASS + 1;

    /**
     * Parameterized types, of type TypeApply.
     */
    public static final int TYPEAPPLY = TYPEARRAY + 1;

    /**
     * Formal type parameters, of type TypeParameter.
     */
    public static final int TYPEPARAMETER = TYPEAPPLY + 1;

    /**
     * Error trees, of type Erroneous.
     */
    public static final int ERRONEOUS = TYPEPARAMETER + 1;

    /**
     * Unary operators, of type Unary.
     */
    public static final int POS = ERRONEOUS + 1;
    public static final int NEG = POS + 1;
    public static final int NOT = NEG + 1;
    public static final int COMPL = NOT + 1;
    public static final int PREINC = COMPL + 1;
    public static final int PREDEC = PREINC + 1;
    public static final int POSTINC = PREDEC + 1;
    public static final int POSTDEC = POSTINC + 1;

    /**
     * unary operator for null reference checks, only used internally.
     */
    public static final int NULLCHK = POSTDEC + 1;

    /**
     * Binary operators, of type Binary.
     */
    public static final int OR = NULLCHK + 1;
    public static final int AND = OR + 1;
    public static final int BITOR = AND + 1;
    public static final int BITXOR = BITOR + 1;
    public static final int BITAND = BITXOR + 1;
    public static final int EQ = BITAND + 1;
    public static final int NE = EQ + 1;
    public static final int LT = NE + 1;
    public static final int GT = LT + 1;
    public static final int LE = GT + 1;
    public static final int GE = LE + 1;
    public static final int SL = GE + 1;
    public static final int SR = SL + 1;
    public static final int USR = SR + 1;
    public static final int PLUS = USR + 1;
    public static final int MINUS = PLUS + 1;
    public static final int MUL = MINUS + 1;
    public static final int DIV = MUL + 1;
    public static final int MOD = DIV + 1;

    public static final int NULL = MOD + 1;
    public static final int CALLEXPR = NULL + 1;
    public static final int THISEXPR = CALLEXPR + 1;
    public static final int READINTEXPR = THISEXPR + 1;
    public static final int READLINEEXPR = READINTEXPR + 1;
    public static final int PRINT = READLINEEXPR + 1;
    
    public static final int REPEATLOOP = PRINT + 1;
    
    public static final int PAINTFORWARD = REPEATLOOP + 1;
    
    public static final int PENUP = PAINTFORWARD + 1;
    
    public static final int PENDOWN = PENUP + 1;
    
    public static final int TURN = PENDOWN + 1;
    
    /**
     * Tags for Literal and TypeLiteral
     */
    public static final int VOID = 0; 
    public static final int INT = VOID + 1; 
    public static final int BOOL = INT + 1; 
    public static final int STRING = BOOL + 1; 


    public Location loc;
    public int tag;
    public Type type;
    
    public static Map<String, FuncDef> funcs;

    /**
     * Initialize tree with given tag.
     */
    public Tree(int tag, Location loc) {
        super();
        this.tag = tag;
        this.loc = loc;
    }

	public Location getLocation() {
		return loc;
	}

    /**
      * Visit this tree with a given visitor.
      */
    public void accept(Visitor v) {
        v.visitTree(this);
    }

	public abstract void printTo(IndentPrintWriter pw);

    public static class TopLevel extends Tree {
		
		public TopLevel(Map<String, FuncDef> func, Location loc) {
			super(TOPLEVEL, loc);
			funcs = func;
		}

    	@Override
        public void accept(Visitor v) {
            v.visitTopLevel(this);
        }

		@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("program");
    		pw.incIndent();
    		for (Map.Entry<String, FuncDef> d : funcs.entrySet()) {
    			d.getValue().printTo(pw);
    		}
    		pw.decIndent();
    	}
    }

    public static class FuncDef extends Tree {
    	
    	public String name;
    	public TypeLiteral returnType;
    	public List<VarDef> formals;
    	public Map<String, VarDef> vars;
    	public Block body;
    	public Functy functy;
    	
        public FuncDef(String name, TypeLiteral returnType,
        		List<VarDef> formals, Map<String, VarDef> vars, Block body, Location loc) {
            super(METHODDEF, loc);
    		this.name = name;
    		this.returnType = returnType;
    		this.formals = formals;
    		this.vars = vars;
    		for (VarDef d : formals)
    			if (vars.containsKey(d.name))
    				Driver.getDriver().issueError(new 
    						DeclConflictError(vars.get(d.name).loc, d.name, d.loc));
    			else
    				vars.put(d.name, d);
    		this.body = body;
       }

        public void accept(Visitor v) {
            v.visitFuncDef(this);
        }
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("func " + name + " ");
    		returnType.printTo(pw);
    		pw.println();
    		pw.incIndent();
    		pw.println("formals");
    		pw.incIndent();
    		for (VarDef d : formals) {
    			d.printTo(pw);
    		}
    		pw.decIndent();
    		pw.println("vars");
    		pw.incIndent();
    		for (Map.Entry<String, VarDef> d : vars.entrySet()) {
    			d.getValue().printTo(pw);
    		}
    		pw.decIndent();
    		body.printTo(pw);
    		pw.decIndent();
    	}
    }

    public static class VarDef extends Tree {
    	
    	public String name;
    	public TypeLiteral type;
    	public Variable variable;

        public VarDef(String name, TypeLiteral type, Location loc) {
            super(VARDEF, loc);
    		this.name = name;
    		this.type = type;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitVarDef(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("vardef " + name + " ");
    		type.printTo(pw);
    		pw.println();
    	}
    }

    /**
      * A no-op statement ";".
      */
    public static class Skip extends Tree {

        public Skip(Location loc) {
            super(SKIP, loc);
        }

    	@Override
        public void accept(Visitor v) {
            v.visitSkip(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		//print nothing
    	}
    }

    public static class Block extends Tree {

    	public List<Tree> block;
 
        public Block(List<Tree> block, Location loc) {
            super(BLOCK, loc);
    		this.block = block;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitBlock(this);
        }
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("stmtblock");
    		pw.incIndent();
    		for (Tree s : block) {
    			s.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }
    
    /**
     * A repeat loop
     */
   public static class RepeatLoop extends Tree {

   	public Expr times;
   	public Tree loopBody;

       public RepeatLoop(Expr times, Tree loopBody, Location loc) {
           super(REPEATLOOP, loc);
           this.times = times;
           this.loopBody = loopBody;
       }

   	@Override
       public void accept(Visitor v) {
           v.visitRepeatLoop(this);
       }

   	@Override
   	public void printTo(IndentPrintWriter pw) {
   		pw.println("repeat");
   		pw.incIndent();
   		times.printTo(pw);
   		if (loopBody != null) {
   			loopBody.printTo(pw);
   		}
   		pw.decIndent();
   	}
  }

    /**
      * A while loop
      */
    public static class WhileLoop extends Tree {

    	public Expr condition;
    	public Tree loopBody;

        public WhileLoop(Expr condition, Tree loopBody, Location loc) {
            super(WHILELOOP, loc);
            this.condition = condition;
            this.loopBody = loopBody;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitWhileLoop(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("while");
    		pw.incIndent();
    		condition.printTo(pw);
    		if (loopBody != null) {
    			loopBody.printTo(pw);
    		}
    		pw.decIndent();
    	}
   }

    /**
      * A for loop.
      */
    public static class ForLoop extends Tree {

    	public Tree init;
    	public Expr condition;
    	public Tree update;
    	public Tree loopBody;

        public ForLoop(Tree init, Expr condition, Tree update,
        		Tree loopBody, Location loc) {
            super(FORLOOP, loc);
    		this.init = init;
    		this.condition = condition;
    		this.update = update;
    		this.loopBody = loopBody;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitForLoop(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("for");
    		pw.incIndent();
    		if (init != null) {
    			init.printTo(pw);
    		} else {
    			pw.println("<emtpy>");
    		}
    		condition.printTo(pw);
    		if (update != null) {
    			update.printTo(pw);
    		} else {
    			pw.println("<empty>");
    		}
    		if (loopBody != null) {
    			loopBody.printTo(pw);
    		}
    		pw.decIndent();
    	}
   }

    /**
      * An "if ( ) { } else { }" block
      */
    public static class If extends Tree {
    	
    	public Expr condition;
    	public Tree trueBranch;
    	public Tree falseBranch;

        public If(Expr condition, Tree trueBranch, Tree falseBranch,
    			Location loc) {
            super(IF, loc);
            this.condition = condition;
    		this.trueBranch = trueBranch;
    		this.falseBranch = falseBranch;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitIf(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("if");
    		pw.incIndent();
    		condition.printTo(pw);
    		if (trueBranch != null) {
    			trueBranch.printTo(pw);
    		}
    		pw.decIndent();
    		if (falseBranch != null) {
    			pw.println("else");
    			pw.incIndent();
    			falseBranch.printTo(pw);
    			pw.decIndent();
    		}
    	}
    }

    /**
      * an expression statement
      * @param expr expression structure
      */
    public static class Exec extends Tree {

    	public Expr expr;

        public Exec(Expr expr, Location loc) {
            super(EXEC, loc);
            this.expr = expr;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitExec(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		expr.printTo(pw);
    	}
    }

    /**
      * A break from a loop.
      */
    public static class Break extends Tree {

        public Break(Location loc) {
            super(BREAK, loc);
        }

    	@Override
        public void accept(Visitor v) {
            v.visitBreak(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("break");
    	}
    }

    /**
      * A return statement.
      */
    public static class Return extends Tree {

    	public Expr expr;

        public Return(Expr expr, Location loc) {
            super(RETURN, loc);
            this.expr = expr;
        }

        @Override
        public void accept(Visitor v) {
            v.visitReturn(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("return");
    		if (expr != null) {
    			pw.incIndent();
    			expr.printTo(pw);
    			pw.decIndent();
    		}
    	}
    }

    public abstract static class Expr extends Tree {
    	
    	public Variable variable;

		public Expr(int tag, Location loc) {
    		super(tag, loc);
    	}
    }

    public abstract static class LValue extends Expr {

    	public enum Kind {
    		LOCAL_VAR, PARAM_VAR, MEMBER_VAR, ARRAY_ELEMENT
    	}
    	public Kind lvKind;
    	
    	LValue(int tag, Location loc) {
    		super(tag, loc);
    	}
    }

    /**
      * A assignment with "=".
      */
    public static class Assign extends Tree {

    	public LValue left;
    	public Expr expr;

        public Assign(LValue left, Expr expr, Location loc) {
            super(ASSIGN, loc);
    		this.left = left;
    		this.expr = expr;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitAssign(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("assign");
    		pw.incIndent();
    		left.printTo(pw);
    		expr.printTo(pw);
    		pw.decIndent();
    	}
    }

    /**
      * A unary operation.
      */
    public static class Unary extends Expr {

    	public Expr expr;

        public Unary(int kind, Expr expr, Location loc) {
            super(kind, loc);
    		this.expr = expr;
        }

    	private void unaryOperatorToString(IndentPrintWriter pw, String op) {
    		pw.println(op);
    		pw.incIndent();
    		expr.printTo(pw);
    		pw.decIndent();
    	}

    	@Override
        public void accept(Visitor v) {
            v.visitUnary(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (tag) {
    		case NEG:
    			unaryOperatorToString(pw, "neg");
    			break;
    		case NOT:
    			unaryOperatorToString(pw, "not");
    			break;
			}
    	}
   }

    /**
      * A binary operation.
      */
    public static class Binary extends Expr {

    	public Expr left;
    	public Expr right;

        public Binary(int kind, Expr left, Expr right, Location loc) {
            super(kind, loc);
    		this.left = left;
    		this.right = right;
        }

    	private void binaryOperatorPrintTo(IndentPrintWriter pw, String op) {
    		pw.println(op);
    		pw.incIndent();
    		left.printTo(pw);
    		right.printTo(pw);
    		pw.decIndent();
    	}

    	@Override
    	public void accept(Visitor visitor) {
    		visitor.visitBinary(this);
    	}

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (tag) {
    		case PLUS:
    			binaryOperatorPrintTo(pw, "add");
    			break;
    		case MINUS:
    			binaryOperatorPrintTo(pw, "sub");
    			break;
    		case MUL:
    			binaryOperatorPrintTo(pw, "mul");
    			break;
    		case DIV:
    			binaryOperatorPrintTo(pw, "div");
    			break;
    		case MOD:
    			binaryOperatorPrintTo(pw, "mod");
    			break;
    		case AND:
    			binaryOperatorPrintTo(pw, "and");
    			break;
    		case OR:
    			binaryOperatorPrintTo(pw, "or");
    			break;
    		case EQ:
    			binaryOperatorPrintTo(pw, "equ");
    			break;
    		case NE:
    			binaryOperatorPrintTo(pw, "neq");
    			break;
    		case LT:
    			binaryOperatorPrintTo(pw, "les");
    			break;
    		case LE:
    			binaryOperatorPrintTo(pw, "leq");
    			break;
    		case GT:
    			binaryOperatorPrintTo(pw, "gtr");
    			break;
    		case GE:
    			binaryOperatorPrintTo(pw, "geq");
    			break;
    		}
    	}
    }

    public static class CallExpr extends Expr {

    	public String func;

    	public List<Expr> actuals;

    	public CallExpr(String func, List<Expr> actuals,
    			Location loc) {
    		super(CALLEXPR, loc);
    		this.func = func;
    		this.actuals = actuals;
    	}

    	@Override
    	public void accept(Visitor visitor) {
    		visitor.visitCallExpr(this);
    	}

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("call " + func);
    		pw.incIndent();
    		
    		for (Expr e : actuals) {
    			e.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }

    /**
      * An identifier
      */
    public static class Ident extends LValue {

    	public String name;
    	public boolean isDefined;

        public Ident(String name, Location loc) {
            super(IDENT, loc);
    		this.name = name;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitIdent(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("varref " + name);
    	}
    }

    /**
      * A constant value given literally.
      * @param value value representation
      */
    public static class Literal extends Expr {

    	public int typeTag;
        public Object value;

        public Literal(int typeTag, Object value, Location loc) {
            super(LITERAL, loc);
            this.typeTag = typeTag;
            this.value = value;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitLiteral(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (typeTag) {
    		case INT:
    			pw.println("intconst " + value);
    			break;
    		case BOOL:
    			pw.println("boolconst " + value);
    			break;
    		}
    	}
    }

    public static abstract class TypeLiteral extends Tree {
    	
    	public TypeLiteral(int tag, Location loc){
    		super(tag, loc);
    	}
    }
    
    /**
      * Identifies a basic type.
      * @param tag the basic type id
      * @see SemanticConstants
      */
    public static class TypeIdent extends TypeLiteral {
    	
        public int typeTag;

        public TypeIdent(int typeTag, Location loc) {
            super(TYPEIDENT, loc);
            this.typeTag = typeTag;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitTypeIdent(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (typeTag){
    		case INT:
    			pw.print("inttype");
    			break;
    		case BOOL:
    			pw.print("booltype");
    			break;
    		case VOID:
    			pw.print("voidtype");
    			break;
    		}
    	}
    }
    
    /**
     * A paint forward statement
     */
   public static class PaintForward extends Tree {

   	public Expr expr;

       public PaintForward(Expr expr, Location loc) {
        super(PAINTFORWARD, loc);
   		this.expr = expr;
       }

   	@Override
       public void accept(Visitor v) {
           v.visitPaintForward(this);
       }

   	@Override
   	public void printTo(IndentPrintWriter pw) {
   		pw.println("paintforward");
   		pw.incIndent();
   		expr.printTo(pw);
   		pw.decIndent();
   	}
   }
   
   /**
    * A turn statement
    */
  public static class Turn extends Tree {

  	public Expr expr;

      public Turn(Expr expr, Location loc) {
        super(TURN, loc);
  		this.expr = expr;
      }

  	@Override
      public void accept(Visitor v) {
          v.visitTurn(this);
      }

  	@Override
  	public void printTo(IndentPrintWriter pw) {
  		pw.println("turn");
  		pw.incIndent();
  		expr.printTo(pw);
  		pw.decIndent();
  	}
  }
  
  /**
   * A pen up statement
   */
 public static class PenUp extends Tree {

     public PenUp(Location loc) {
       super(PENUP, loc);
     }

 	@Override
     public void accept(Visitor v) {
         v.visitPenUp(this);
     }

 	@Override
 	public void printTo(IndentPrintWriter pw) {
 		pw.println("penup");
 	}
 }
 
 /**
  * A pen down statement
  */
public static class PenDown extends Tree {

    public PenDown(Location loc) {
      super(PENDOWN, loc);
    }

	@Override
    public void accept(Visitor v) {
        v.visitPenDown(this);
    }

	@Override
	public void printTo(IndentPrintWriter pw) {
		pw.println("pendown");
	}
}

    /**
      * A generic visitor class for trees.
      */
    public static abstract class Visitor {

        public Visitor() {
            super();
        }

        public void visitTopLevel(TopLevel that) {
            visitTree(that);
        }

        public void visitFuncDef(FuncDef that) {
            visitTree(that);
        }

        public void visitVarDef(VarDef that) {
            visitTree(that);
        }

        public void visitSkip(Skip that) {
            visitTree(that);
        }

        public void visitBlock(Block that) {
            visitTree(that);
        }
        
        public void visitRepeatLoop(RepeatLoop that) {
            visitTree(that);
        }

        public void visitWhileLoop(WhileLoop that) {
            visitTree(that);
        }

        public void visitForLoop(ForLoop that) {
            visitTree(that);
        }

        public void visitIf(If that) {
            visitTree(that);
        }

        public void visitExec(Exec that) {
            visitTree(that);
        }

        public void visitBreak(Break that) {
            visitTree(that);
        }

        public void visitReturn(Return that) {
            visitTree(that);
        }

        public void visitAssign(Assign that) {
            visitTree(that);
        }

        public void visitUnary(Unary that) {
            visitTree(that);
        }

        public void visitBinary(Binary that) {
            visitTree(that);
        }

        public void visitCallExpr(CallExpr that) {
            visitTree(that);
        }

        public void visitLValue(LValue that) {
            visitTree(that);
        }

        public void visitIdent(Ident that) {
            visitTree(that);
        }

        public void visitLiteral(Literal that) {
            visitTree(that);
        }

        public void visitTypeIdent(TypeIdent that) {
            visitTree(that);
        }
        
        public void visitPaintForward(PaintForward that) {
            visitTree(that);
        }
        
        public void visitTurn(Turn that) {
            visitTree(that);
        }
        
        public void visitPenUp(PenUp that) {
        	visitTree(that);
        }
        
        public void visitPenDown(PenDown that) {
            visitTree(that);
        }

        public void visitTree(Tree that) {
            assert false;
        }
    }
}
