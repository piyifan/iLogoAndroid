package com.iLogo.iCode;

public class Constant extends Variable {
	
	public int number;
	
	public Constant(int tag, int number)
	{
		super(tag);
		this.number = number;
	}
	
	@Override
	public boolean isConstant()
	{
		return true;
	}
	
	@Override
	public String toString()
	{
		return "[" + String.valueOf(number) + "]";
	}
	
}
