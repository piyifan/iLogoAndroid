package com.iLogo.type;

import com.iLogo.tree.Tree;

public class BaseType extends Type {

	private final String typeName;

	private BaseType(String typeName) {
		this.typeName = typeName;
	}

	public static final BaseType INT = new BaseType("int");
	
	public static final BaseType BOOL = new BaseType("bool");

	public static final BaseType ERROR = new BaseType("Error");
	
	public static final BaseType VOID = new BaseType("void");
	
	public static BaseType getType(Tree.TypeLiteral type) {
		switch (((Tree.TypeIdent)type).typeTag){
		   case Tree.VOID : return VOID;
		   case Tree.INT  : return INT;
		   case Tree.BOOL : return BOOL;
		   default: return ERROR;
		}
	}

	@Override
	public boolean isBaseType() {
		return true;
	}

	@Override
	public boolean compatible(Type type) {
		if (equal(ERROR) || type.equal(ERROR)) {
			return true;
		}
		return equal(type);
	}

	@Override
	public boolean equal(Type type) {
		return this == type;
	}

	@Override
	public String toString() {
		return typeName;
	}

}
