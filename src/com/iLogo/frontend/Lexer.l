package frontend;

import iLogo.Location;
import error.*;
 
%%
%public
%class Lexer
%extends BaseLexer
%byaccj
%line
%column
%switch
%unicode
	
%{
	public Location getLocation() {
		return new iLogo.Location(yyline + 1, yycolumn + 1);
	}
%}

NEWLINE				= (\r|\n|\r\n)
DIGIT 				= ([0-9])
INTEGER				= ({DIGIT}+)
IDENTIFIER			= ([A-Za-z][_0-9A-Za-z]*)
UNARY_OPERATOR		= ("+"|"-"|"*"|"/"|"%"|"="|"<"|">"|"."|","|";"|"!"|"("|")"|"["|"]"|"{"|"}")
S_COMMENT			= ("//"[^\r\n]*{NEWLINE})
WHITESPACE			= ([ \t]+)

%x S

%%

{WHITESPACE}		{ /* Just ignore */    }
{NEWLINE}			{ /* Just ignore */    }
{S_COMMENT}			{ /* Just ignore */    }


"break"              {return keyword(Parser.BREAK);}
"else"               {return keyword(Parser.ELSE);}
"for"              	 {return keyword(Parser.FOR);}
"if"              	 {return keyword(Parser.IF);}
"int"              	 {return keyword(Parser.INT);}
"bool"               {return keyword(Parser.BOOL);}
"return"             {return keyword(Parser.RETURN);}
"void"				 {return keyword(Parser.VOID);}
"while"              {return keyword(Parser.WHILE);}
"repeat"             {return keyword(Parser.REPEAT);}
"var"                {return keyword(Parser.VAR);}

"pu"                 {return keyword(Parser.PU);}
"pd"				 {return keyword(Parser.PD);}
"fw"				 {return keyword(Parser.FW);}
"rt"				 {return keyword(Parser.RT);}

"&&"                { return operator(Parser.AND); }
"=="                { return operator(Parser.EQUAL); }
">="                { return operator(Parser.GREATER_EQUAL); }
"<="				{ return operator(Parser.LESS_EQUAL); }
"!="                { return operator(Parser.NOT_EQUAL); }
"||"                { return operator(Parser.OR); }

{UNARY_OPERATOR}    { return operator( (byte)yytext().charAt(0) ); }

{INTEGER}           { return  intConst(yytext());    }
"true"				{ return boolConst(true); }
"false"				{ return boolConst(false); }

{IDENTIFIER}        { return identifier( yytext() ); }

.                   { issueError(new UnrecogCharError(getLocation(), yytext().charAt(0))); }