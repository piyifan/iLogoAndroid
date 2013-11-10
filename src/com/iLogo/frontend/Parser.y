%{
package frontend;

import tree.Tree;
import tree.Tree.*;
import error.*;
import java.util.*;
import iLogo.Driver;
%}

%Jclass Parser
%Jextends BaseParser
%Jsemantic SemValue
%Jnorun
%Jnodebug
%Jnoconstruct

%token VOID   INT  BOOL
%token REPEAT WHILE   FOR   
%token IF ELSE RETURN BREAK
%token LITERAL
%token VAR
%token IDENTIFIER	  AND    OR
%token FW RT PU PD
%token LESS_EQUAL   GREATER_EQUAL  EQUAL   NOT_EQUAL
%token '+'  '-'  '*'  '/'  '%'  '='  '>'  '<'  '.'
%token ','  ';'  '!'  '('  ')'  '['  ']'  '{'  '}'

%left OR
%left AND 
%nonassoc EQUAL NOT_EQUAL
%nonassoc LESS_EQUAL GREATER_EQUAL '<' '>'
%left  '+' '-'
%left  '*' '/' '%'  
%nonassoc UMINUS '!' 
%nonassoc '[' '.' 
%nonassoc ')' EMPTY
%nonassoc ELSE

%start Program

%%
Program			:	FuncList
					{
						tree = new Tree.TopLevel($1.fmap, $1.loc);
					}
				;

FuncList       :	FuncList FuncDef
					{
						if ($$.fmap.containsKey($2.fdef.name))
							Driver.getDriver().issueError(new 
    						   DeclConflictError($$.fmap.get($2.fdef.name).loc, $2.fdef.name, $2.fdef.loc));
						else
						     $$.fmap.put($2.fdef.name, $2.fdef);
					}
                |	FuncDef
                	{
                		$$.fmap = new LinkedHashMap<String, FuncDef>();
                		$$.fmap.put($1.fdef.name, $1.fdef);
                	}
                ;

VariableDef     :	Variable ';'
				;

Variable        :	Type IDENTIFIER
					{
						$$.vdef = new Tree.VarDef($2.ident, $1.type, $2.loc);
					}
				;
				
Type            :	INT
					{
						$$.type = new Tree.TypeIdent(Tree.INT, $1.loc);
					}
                |	VOID
                	{
                		$$.type = new Tree.TypeIdent(Tree.VOID, $1.loc);
                	}
                |	BOOL
                	{
                		$$.type = new Tree.TypeIdent(Tree.BOOL, $1.loc);
                	}
                ;

Formals         :	VariableList
                |	/* empty */
                	{
                		$$ = new SemValue();
                		$$.vlist = new ArrayList<Tree.VarDef>(); 
                	}
                ;

VariableList    :	VariableList ',' Variable
					{
						$$.vlist.add($3.vdef);
					}
                |	Variable
                	{
                		$$.vlist = new ArrayList<Tree.VarDef>();
						$$.vlist.add($1.vdef);
                	}
                ;
				
VarDeclare      :   VarDeclare VariableDef
					{
						if ($$.vmap.containsKey($2.vdef.name))
							Driver.getDriver().issueError(new 
    						   DeclConflictError($$.vmap.get($2.vdef.name).loc, $2.vdef.name, $2.vdef.loc));
						else
						     $$.vmap.put($2.vdef.name, $2.vdef);
					}
				|	VAR
					{
                		$$.vmap = new LinkedHashMap<String, VarDef>();
					}
				;

FuncDef         :	Type IDENTIFIER '(' Formals ')' VarDeclare StmtBlock
					{
						$$.fdef = new FuncDef($2.ident, $1.type, $4.vlist, $6.vmap, (Block) $7.stmt, $2.loc);
					}
				|	Type IDENTIFIER '(' Formals ')' StmtBlock
					{
						$$.fdef = new FuncDef($2.ident, $1.type, $4.vlist, new LinkedHashMap<String, VarDef>(), (Block) $6.stmt, $2.loc);
					}
                ;

StmtBlock       :	'{' StmtList '}'
					{
						$$.stmt = new Block($2.slist, $1.loc);
					}
                ;
	
StmtList        :	StmtList Stmt
					{
						$$.slist.add($2.stmt);
					}
                |	/* empty */
                	{
                		$$ = new SemValue();
                		$$.slist = new ArrayList<Tree>();
                	}
                ;

Stmt		    :	SimpleStmt ';'
                	{
                		if ($$.stmt == null) {
                			$$.stmt = new Tree.Skip($2.loc);
                		}
                	}
                |	IfStmt
                |	WhileStmt
                |	ForStmt
				|   RepeatStmt
                |	ReturnStmt ';'
                |	BreakStmt ';'
                |	StmtBlock
                ;

SimpleStmt      :	LValue '=' Expr
					{
						$$.stmt = new Tree.Assign($1.lvalue, $3.expr, $2.loc);
					}
                |	Call
                	{
                		$$.stmt = new Tree.Exec($1.expr, $1.loc);
                	}
				|   FW Expr
					{
						$$.stmt = new Tree.PaintForward($2.expr, $1.loc);
					}
				|   RT Expr
					{
						$$.stmt = new Tree.Turn($2.expr, $1.loc);
					}
				|   PU
					{
						$$.stmt = new Tree.PenUp($1.loc);
					}
				|	PD
					{
						$$.stmt = new Tree.PenDown($1.loc);
					}                
				|	/* empty */
                	{
                		$$ = new SemValue();
                	}
                ;

LValue          :	IDENTIFIER
					{
						$$.lvalue = new Tree.Ident($1.ident, $1.loc);
						$$.loc = $1.loc;
					}
                ;

Call            :	IDENTIFIER '(' Actuals ')'
					{
						$$.expr = new Tree.CallExpr($1.ident, $3.elist, $1.loc);
						$$.loc = $1.loc;
					}
                ;

Expr            :	LValue
					{
						$$.expr = $1.lvalue;
					}
                |	Call
                |	Constant
                |	Expr '+' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.PLUS, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '-' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.MINUS, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '*' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.MUL, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '/' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.DIV, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '%' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.MOD, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr EQUAL Expr
                	{
                		$$.expr = new Tree.Binary(Tree.EQ, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr NOT_EQUAL Expr
                	{
                		$$.expr = new Tree.Binary(Tree.NE, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '<' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.LT, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr '>' Expr
                	{
                		$$.expr = new Tree.Binary(Tree.GT, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr LESS_EQUAL Expr
                	{
                		$$.expr = new Tree.Binary(Tree.LE, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr GREATER_EQUAL Expr
                	{
                		$$.expr = new Tree.Binary(Tree.GE, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr AND Expr
                	{
                		$$.expr = new Tree.Binary(Tree.AND, $1.expr, $3.expr, $2.loc);
                	}
                |	Expr OR Expr
                	{
                		$$.expr = new Tree.Binary(Tree.OR, $1.expr, $3.expr, $2.loc);
                	}
                |	'(' Expr ')'
                	{
                		$$ = $2;
                	}
                |	'-' Expr  				%prec UMINUS
                	{
                		$$.expr = new Tree.Unary(Tree.NEG, $2.expr, $1.loc);
                	}
                |	'!' Expr
                	{
                		$$.expr = new Tree.Unary(Tree.NOT, $2.expr, $1.loc);
                	}
                ;
	
Constant        :	LITERAL
					{
						$$.expr = new Tree.Literal($1.typeTag, $1.literal, $1.loc);
					}
                ;

Actuals         :	ExprList
                |	/* empty */
                	{
                		$$ = new SemValue();
                		$$.elist = new ArrayList<Tree.Expr>();
                	}
                ;

ExprList        :	ExprList ',' Expr
					{
						$$.elist.add($3.expr);
					}
                |	Expr
                	{
                		$$.elist = new ArrayList<Tree.Expr>();
						$$.elist.add($1.expr);
                	}
                ;
				
RepeatStmt      :   REPEAT '(' Expr ')' Stmt
					{
						$$.stmt = new Tree.RepeatLoop($3.expr, $5.stmt, $1.loc);
					}
				;
    
WhileStmt       :	WHILE '(' Expr ')' Stmt
					{
						$$.stmt = new Tree.WhileLoop($3.expr, $5.stmt, $1.loc);
					}
                ;

ForStmt         :	FOR '(' SimpleStmt ';' Expr ';'	SimpleStmt ')' Stmt
					{
						$$.stmt = new Tree.ForLoop($3.stmt, $5.expr, $7.stmt, $9.stmt, $1.loc);
					}
                ;

BreakStmt       :	BREAK
					{
						$$.stmt = new Tree.Break($1.loc);
					}
                ;

IfStmt          :	IF '(' Expr ')' Stmt ElseClause
					{
						$$.stmt = new Tree.If($3.expr, $5.stmt, $6.stmt, $1.loc);
					}
                ;

ElseClause      :	ELSE Stmt
					{
						$$.stmt = $2.stmt;
					}
				|	/* empty */				%prec EMPTY
					{
						$$ = new SemValue();
					}
                ;

ReturnStmt      :	RETURN Expr
					{
						$$.stmt = new Tree.Return($2.expr, $1.loc);
					}
                |	RETURN
                	{
                		$$.stmt = new Tree.Return(null, $1.loc);
                	}
                ;

%%