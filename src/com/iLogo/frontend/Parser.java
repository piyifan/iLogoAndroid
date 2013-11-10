//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "Parser.y"
package com.iLogo.frontend;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.iLogo.driver.Driver;
import com.iLogo.error.DeclConflictError;
import com.iLogo.tree.Tree;
import com.iLogo.tree.Tree.Block;
import com.iLogo.tree.Tree.FuncDef;
import com.iLogo.tree.Tree.VarDef;




public class Parser
             extends BaseParser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
final SemValue dup_yyval(SemValue val)
{
  return val;
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short INT=258;
public final static short BOOL=259;
public final static short REPEAT=260;
public final static short WHILE=261;
public final static short FOR=262;
public final static short IF=263;
public final static short ELSE=264;
public final static short RETURN=265;
public final static short BREAK=266;
public final static short LITERAL=267;
public final static short VAR=268;
public final static short IDENTIFIER=269;
public final static short AND=270;
public final static short OR=271;
public final static short FW=272;
public final static short RT=273;
public final static short PU=274;
public final static short PD=275;
public final static short LESS_EQUAL=276;
public final static short GREATER_EQUAL=277;
public final static short EQUAL=278;
public final static short NOT_EQUAL=279;
public final static short UMINUS=280;
public final static short EMPTY=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    6,    6,
    7,    7,    8,    8,    2,    2,    9,   10,   10,   11,
   11,   11,   11,   11,   11,   11,   11,   12,   12,   12,
   12,   12,   12,   12,   19,   21,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   23,   22,   22,   24,   24,
   16,   14,   15,   18,   13,   25,   25,   17,   17,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    0,
    3,    1,    2,    1,    7,    6,    3,    2,    0,    2,
    1,    1,    1,    1,    2,    2,    1,    3,    1,    2,
    2,    1,    1,    0,    1,    4,    1,    1,    1,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    2,    2,    1,    1,    0,    3,    1,
    5,    5,    9,    1,    6,    2,    0,    2,    1,
};
final static short yydefred[] = {                         0,
    7,    6,    8,    0,    0,    3,    0,    2,    0,    0,
   12,    0,    0,    0,    5,    0,    0,   14,   19,    0,
   16,   11,    0,   13,    0,   15,    0,    0,    0,    0,
    0,   64,    0,    0,    0,   32,   33,   17,   27,   18,
    0,   21,   22,   23,   24,    0,    0,    0,   29,    4,
    0,    0,    0,    0,   56,    0,    0,    0,   37,    0,
   38,   39,    0,    0,    0,   20,   25,   26,    0,    0,
    0,    0,    0,   54,   55,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   53,    0,    0,
    0,    0,    0,    0,    0,    0,   42,   43,   44,    0,
    0,   36,    0,   61,   62,    0,    0,    0,    0,    0,
   65,    0,   66,    0,   63,
};
final static short yydgoto[] = {                          4,
    5,    6,   24,   11,   12,   13,   14,   20,   39,   23,
   40,   41,   42,   43,   44,   45,   46,   47,   59,   60,
   61,   91,   62,   92,  121,
};
final static short yysindex[] = {                      -158,
    0,    0,    0,    0, -158,    0, -268,    0,  -27, -158,
    0, -249,  -13,   -5,    0, -111, -158,    0,    0,  -78,
    0,    0,  150,    0,  -18,    0,   24,   33,   62,   65,
  -31,    0,   67,  -31,  -31,    0,    0,    0,    0,    0,
   31,    0,    0,    0,    0,   52,   54,   55,    0,    0,
  -31,  -31,  -49,  -31,    0,  -31,  -31,  -31,    0,   34,
    0,    0,  -31,   34,   34,    0,    0,    0,  -31,  -26,
  -10,   59,    1,    0,    0,   12,  -31,  -31,  -31,  -31,
  -31,  -31,  -31,  -31,  -31,  -31,  -31,  -31,  -31,   34,
   79,   83,   34,  169,  169,  -31,  169,    0,   61,   50,
  149,  149,   72,   72,  156,  156,    0,    0,    0,  149,
  149,    0,  -31,    0,    0,   23, -136,   34,  -49,  169,
    0,   89,    0,  169,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  138,    0,    0,    0,    0,   90,
    0,    0,    0,   98,    0,    0,    0,    0,    0,    0,
    0,    0,   70,    0,    0,    0,    0,    0,    0,    0,
   86,    0,  -37,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   70,    0,    0,    0,    0,    0,    0,   54,
    0,    0,  101,  -12,   -1,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   45,
    0,  107,   21,   70,   70,    0,   70,    0,  119,  131,
  103,  105,  125,  129,   81,   92,    0,    0,    0,  109,
  115,    0,    0,    0,    0,    0,  -56,   47,  114,   70,
    0,    0,    0,   70,    0,
};
final static short yygindex[] = {                         0,
    0,  153,    0,   58,   51,    0,    0,    0,   10,    0,
   88,  -35,    0,    0,    0,    0,    0,    0,  212,  402,
  354,    0,    0,    0,    0,
};
final static int YYTABLESIZE=515;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
    9,   57,   67,   35,   35,   35,   35,   35,   58,   35,
   87,   19,   10,   56,   94,   85,   83,   72,   84,   15,
   86,   35,   35,   35,   35,   21,   87,   16,   30,   26,
   95,   85,   83,   89,   84,   88,   86,   87,   17,   31,
   50,   97,   85,   83,   19,   84,   30,   86,   87,   89,
    7,   88,   98,   85,   83,    7,   84,   31,   86,   87,
   89,   28,   88,   51,   85,   83,   67,   84,   67,   86,
   87,   89,   52,   88,   22,   85,   83,   25,   84,   28,
   86,  119,   89,  122,   88,   60,   87,   59,   60,   66,
   59,   85,   83,   89,   84,   88,   86,   87,    1,    2,
    3,   53,   85,   83,   54,   84,   63,   86,   87,   89,
   67,   88,   68,   85,   83,   69,   84,   96,   86,  112,
   89,   40,   88,   40,   40,   40,  113,  120,   34,  124,
   10,   89,   41,   88,   41,   41,   41,    1,    9,   40,
   40,   58,   40,   49,   69,   50,   49,   57,   50,   48,
   41,   41,   48,   41,   34,   47,   18,    8,   47,   51,
    0,   49,   51,   50,    0,   45,    0,   48,   45,   46,
    0,   52,   46,   47,   52,    0,    0,   51,    1,    2,
    3,  114,  115,   45,  117,   87,    0,   46,    0,   52,
   85,   83,   87,   84,    0,   86,    0,   85,    0,    0,
    0,    0,   86,   67,   67,   67,   67,  123,   67,   67,
    0,  125,   67,    0,    0,   67,   67,   67,   67,   33,
    0,    0,   34,   35,   36,   37,    0,    0,    0,    0,
    0,    0,   35,   35,   48,   55,    0,   33,   35,   35,
   35,   35,    0,   77,   78,    0,    0,    0,    0,   79,
   80,   81,   82,    0,    0,    0,    0,    0,    0,   77,
   78,    0,    0,    0,   48,   79,   80,   81,   82,    0,
   77,   78,   19,    0,   38,    0,   79,   80,   81,   82,
    0,   77,   78,    0,    0,    0,    0,   79,   80,   81,
   82,   19,   77,   78,    0,    0,    0,    0,   79,   80,
   81,   82,    0,   77,   78,   48,   48,    0,   48,   79,
   80,   81,   82,    0,    0,    0,    0,    0,    0,   77,
    0,    0,    0,    0,    0,   79,   80,   81,   82,    0,
   48,   48,    0,    0,    0,   48,   79,   80,   81,   82,
    0,    0,    0,    0,    0,    0,    0,   79,   80,    0,
   40,   40,    0,    0,    0,    0,   40,   40,   40,   40,
    0,   41,   41,    0,    0,    0,    0,   41,   41,   41,
   41,    0,   49,   49,   50,   50,   49,    0,   48,   48,
   49,   49,   50,   50,   47,   47,   48,   48,   51,   51,
    0,    0,   47,   47,   45,   45,    0,    0,   46,   46,
    0,   52,    0,    0,    0,    0,   49,    0,    0,   27,
   28,   29,   30,    0,   31,   32,    0,    0,   33,    0,
    0,   34,   35,   36,   37,    0,    0,    0,   27,   28,
   29,   30,    0,   31,   32,   64,   65,   33,    0,    0,
   34,   35,   36,   37,    0,    0,    0,   49,   49,    0,
   49,    0,   70,   71,    0,   73,    0,   74,   75,   76,
    0,    0,    0,    0,   90,    0,    0,    0,    0,    0,
   93,    0,   49,   49,    0,    0,    0,   49,   99,  100,
  101,  102,  103,  104,  105,  106,  107,  108,  109,  110,
  111,    0,    0,    0,    0,    0,    0,  116,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  118,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
  269,   33,   59,   41,   42,   43,   44,   45,   40,   47,
   37,  123,   40,   45,   41,   42,   43,   53,   45,  269,
   47,   59,   60,   61,   62,   16,   37,   41,   41,   20,
   41,   42,   43,   60,   45,   62,   47,   37,   44,   41,
   59,   41,   42,   43,  123,   45,   59,   47,   37,   60,
    0,   62,   41,   42,   43,    5,   45,   59,   47,   37,
   60,   41,   62,   40,   42,   43,  123,   45,  125,   47,
   37,   60,   40,   62,   17,   42,   43,   20,   45,   59,
   47,   59,   60,  119,   62,   41,   37,   41,   44,   59,
   44,   42,   43,   60,   45,   62,   47,   37,  257,  258,
  259,   40,   42,   43,   40,   45,   40,   47,   37,   60,
   59,   62,   59,   42,   43,   61,   45,   59,   47,   41,
   60,   41,   62,   43,   44,   45,   44,  264,   59,   41,
   41,   60,   41,   62,   43,   44,   45,    0,   41,   59,
   60,   41,   62,   41,   59,   41,   44,   41,   44,   41,
   59,   60,   44,   62,   41,   41,  268,    5,   44,   41,
   -1,   59,   44,   59,   -1,   41,   -1,   59,   44,   41,
   -1,   41,   44,   59,   44,   -1,   -1,   59,  257,  258,
  259,   94,   95,   59,   97,   37,   -1,   59,   -1,   59,
   42,   43,   37,   45,   -1,   47,   -1,   42,   -1,   -1,
   -1,   -1,   47,  260,  261,  262,  263,  120,  265,  266,
   -1,  124,  269,   -1,   -1,  272,  273,  274,  275,  269,
   -1,   -1,  272,  273,  274,  275,   -1,   -1,   -1,   -1,
   -1,   -1,  270,  271,   23,  267,   -1,  269,  276,  277,
  278,  279,   -1,  270,  271,   -1,   -1,   -1,   -1,  276,
  277,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,  270,
  271,   -1,   -1,   -1,   53,  276,  277,  278,  279,   -1,
  270,  271,  123,   -1,  125,   -1,  276,  277,  278,  279,
   -1,  270,  271,   -1,   -1,   -1,   -1,  276,  277,  278,
  279,  123,  270,  271,   -1,   -1,   -1,   -1,  276,  277,
  278,  279,   -1,  270,  271,   94,   95,   -1,   97,  276,
  277,  278,  279,   -1,   -1,   -1,   -1,   -1,   -1,  270,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,   -1,
  119,  120,   -1,   -1,   -1,  124,  276,  277,  278,  279,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  276,  277,   -1,
  270,  271,   -1,   -1,   -1,   -1,  276,  277,  278,  279,
   -1,  270,  271,   -1,   -1,   -1,   -1,  276,  277,  278,
  279,   -1,  270,  271,  270,  271,   23,   -1,  270,  271,
  278,  279,  278,  279,  270,  271,  278,  279,  270,  271,
   -1,   -1,  278,  279,  270,  271,   -1,   -1,  270,  271,
   -1,  271,   -1,   -1,   -1,   -1,   53,   -1,   -1,  260,
  261,  262,  263,   -1,  265,  266,   -1,   -1,  269,   -1,
   -1,  272,  273,  274,  275,   -1,   -1,   -1,  260,  261,
  262,  263,   -1,  265,  266,   34,   35,  269,   -1,   -1,
  272,  273,  274,  275,   -1,   -1,   -1,   94,   95,   -1,
   97,   -1,   51,   52,   -1,   54,   -1,   56,   57,   58,
   -1,   -1,   -1,   -1,   63,   -1,   -1,   -1,   -1,   -1,
   69,   -1,  119,  120,   -1,   -1,   -1,  124,   77,   78,
   79,   80,   81,   82,   83,   84,   85,   86,   87,   88,
   89,   -1,   -1,   -1,   -1,   -1,   -1,   96,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  113,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","INT","BOOL","REPEAT",
"WHILE","FOR","IF","ELSE","RETURN","BREAK","LITERAL","VAR","IDENTIFIER","AND",
"OR","FW","RT","PU","PD","LESS_EQUAL","GREATER_EQUAL","EQUAL","NOT_EQUAL",
"UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : FuncList",
"FuncList : FuncList FuncDef",
"FuncList : FuncDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"VarDeclare : VarDeclare VariableDef",
"VarDeclare : VAR",
"FuncDef : Type IDENTIFIER '(' Formals ')' VarDeclare StmtBlock",
"FuncDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : RepeatStmt",
"Stmt : ReturnStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt : FW Expr",
"SimpleStmt : RT Expr",
"SimpleStmt : PU",
"SimpleStmt : PD",
"SimpleStmt :",
"LValue : IDENTIFIER",
"Call : IDENTIFIER '(' Actuals ')'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Constant : LITERAL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"RepeatStmt : REPEAT '(' Expr ')' Stmt",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
};

//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 44 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).fmap, val_peek(0).loc);
					}
break;
case 2:
//#line 50 "Parser.y"
{
						if (yyval.fmap.containsKey(val_peek(0).fdef.name))
							Driver.getDriver().issueError(new 
    						   DeclConflictError(yyval.fmap.get(val_peek(0).fdef.name).loc, val_peek(0).fdef.name, val_peek(0).fdef.loc));
						else
						     yyval.fmap.put(val_peek(0).fdef.name, val_peek(0).fdef);
					}
break;
case 3:
//#line 58 "Parser.y"
{
                		yyval.fmap = new LinkedHashMap<String, FuncDef>();
                		yyval.fmap.put(val_peek(0).fdef.name, val_peek(0).fdef);
                	}
break;
case 5:
//#line 68 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 74 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 78 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 82 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 10:
//#line 89 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 11:
//#line 96 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 12:
//#line 100 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 13:
//#line 107 "Parser.y"
{
						if (yyval.vmap.containsKey(val_peek(0).vdef.name))
							Driver.getDriver().issueError(new 
    						   DeclConflictError(yyval.vmap.get(val_peek(0).vdef.name).loc, val_peek(0).vdef.name, val_peek(0).vdef.loc));
						else
						     yyval.vmap.put(val_peek(0).vdef.name, val_peek(0).vdef);
					}
break;
case 14:
//#line 115 "Parser.y"
{
                		yyval.vmap = new LinkedHashMap<String, VarDef>();
					}
break;
case 15:
//#line 121 "Parser.y"
{
						yyval.fdef = new FuncDef(val_peek(5).ident, val_peek(6).type, val_peek(3).vlist, val_peek(1).vmap, (Block) val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 16:
//#line 125 "Parser.y"
{
						yyval.fdef = new FuncDef(val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, new LinkedHashMap<String, VarDef>(), (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 17:
//#line 131 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 18:
//#line 137 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 19:
//#line 141 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 148 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 28:
//#line 163 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 29:
//#line 167 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 30:
//#line 171 "Parser.y"
{
						yyval.stmt = new Tree.PaintForward(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 31:
//#line 175 "Parser.y"
{
						yyval.stmt = new Tree.Turn(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 32:
//#line 179 "Parser.y"
{
						yyval.stmt = new Tree.PenUp(val_peek(0).loc);
					}
break;
case 33:
//#line 183 "Parser.y"
{
						yyval.stmt = new Tree.PenDown(val_peek(0).loc);
					}
break;
case 34:
//#line 187 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 35:
//#line 193 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(0).ident, val_peek(0).loc);
						yyval.loc = val_peek(0).loc;
					}
break;
case 36:
//#line 200 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						yyval.loc = val_peek(3).loc;
					}
break;
case 37:
//#line 207 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 40:
//#line 213 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 41:
//#line 217 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 42:
//#line 221 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 43:
//#line 225 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 44:
//#line 229 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 45:
//#line 233 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 46:
//#line 237 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 47:
//#line 241 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 48:
//#line 245 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 49:
//#line 249 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 50:
//#line 253 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 51:
//#line 257 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 52:
//#line 261 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 53:
//#line 265 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 54:
//#line 269 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 55:
//#line 273 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 56:
//#line 279 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 58:
//#line 286 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 59:
//#line 293 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 60:
//#line 297 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 61:
//#line 304 "Parser.y"
{
						yyval.stmt = new Tree.RepeatLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 62:
//#line 310 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 63:
//#line 316 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 64:
//#line 322 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 65:
//#line 328 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 66:
//#line 334 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 67:
//#line 338 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 68:
//#line 344 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 69:
//#line 348 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
//#line 915 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
