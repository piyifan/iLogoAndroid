package com.iLogo.type;

public abstract class Type {
	public boolean isBaseType() {
		return false;
	}
	
	public boolean isFuncType() {
		return false;
	}

	public abstract boolean compatible(Type type);

	public abstract boolean equal(Type type);

	public abstract String toString();
}
