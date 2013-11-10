package com.iLogo.frontend;

import java.util.List;
import java.util.Map;

import com.iLogo.driver.Location;
import com.iLogo.tree.Tree.*;
import com.iLogo.tree.Tree;


public class SemValue {

	public int code;

	public Location loc;

	public int typeTag;
	
	public Object literal;
	
	public String ident;


	public Map<String, FuncDef> fmap;

	public List<VarDef> vlist;
	public Map<String, VarDef> vmap;



	public List<Tree> slist;

	public List<Expr> elist;

	public TopLevel prog;

	public VarDef vdef;

	public FuncDef fdef;

	public TypeLiteral type;

	public Tree stmt;

	public Expr expr;

	public LValue lvalue;

	/**
	 * 创建一个关键字的语义值
	 * 
	 * @param code
	 *            关键字的代表码
	 * @return 对应关键字的语义值
	 */
	public static SemValue createKeyword(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 创建一个操作符的语义值
	 * 
	 * @param code
	 *            操作符的代表码
	 * @return 对应操作符的语义值
	 */
	public static SemValue createOperator(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 创建一个常量的语义值
	 * 
	 * @param value
	 *            常量的值
	 * @return 对应的语义值
	 */
	public static SemValue createLiteral(int tag, Object value) {
		SemValue v = new SemValue();
		v.code = Parser.LITERAL;
		v.typeTag = tag;
		v.literal = value;
		return v;
	}

	/**
	 * 创建一个标识符的语义值
	 * 
	 * @param name
	 *            标识符的名字
	 * @return 对应的语义值（标识符名字存放在sval域）
	 */
	public static SemValue createIdentifier(String name) {
		SemValue v = new SemValue();
		v.code = Parser.IDENTIFIER;
		v.ident = name;
		return v;
	}

	/**
	 * 获取这个语义值的字符串表示<br>
	 */
	public String toString() {
		String msg;
		switch (code) {
		// 关键字
		case Parser.BOOL:
			msg = "keyword  : bool";
			break;
		case Parser.BREAK:
			msg = "keyword  : break";
			break;
		case Parser.ELSE:
			msg = "keyword  : else";
			break;
		case Parser.FOR:
			msg = "keyword  : for";
			break;
		case Parser.IF:
			msg = "keyword  : if";
			break;
		case Parser.INT:
			msg = "keyword  : int";
			break;
		case Parser.RETURN:
			msg = "keyword  : return";
			break;
		case Parser.VOID:
			msg = "keyword  : void";
			break;
		case Parser.WHILE:
			msg = "keyword  : while";
			break;
		case Parser.FW:
			msg = "keyword  : fw";
			break;
		case Parser.RT:
			msg = "keyword  : rt";
			break;
		case Parser.PU:
			msg = "keyword  : pu";
			break;
		case Parser.PD:
			msg = "keyword  : pd";
			break;
		case Parser.REPEAT:
			msg = "keyword  : repeat";
			break;

		// 常量
		case Parser.LITERAL:
			switch (typeTag) {
			case Tree.INT:
			case Tree.BOOL:
				msg = "constant : " + literal;
				break;
			}
		// 标识符
		case Parser.IDENTIFIER:
			msg = "identifier: " + ident;
			break;

		// 操作符
		case Parser.AND:
			msg = "operator : &&";
			break;
		case Parser.EQUAL:
			msg = "operator : ==";
			break;
		case Parser.GREATER_EQUAL:
			msg = "operator : >=";
			break;
		case Parser.LESS_EQUAL:
			msg = "operator : <=";
			break;
		case Parser.NOT_EQUAL:
			msg = "operator : !=";
			break;
		case Parser.OR:
			msg = "operator : ||";
			break;
		default:
			msg = "operator : " + (char) code;
			break;
		}
		return (String.format("%-15s%s", loc, msg));
	}
}
