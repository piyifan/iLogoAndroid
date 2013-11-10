package com.iLogo.iCode;

public class Variable {
	
	public int tag;
	
	public Variable(int tag)
	{
		this.tag = tag;
	}
	
	public boolean isConstant()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return "_" + String.valueOf(tag);
	}
}
