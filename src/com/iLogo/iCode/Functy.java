package com.iLogo.iCode;

import java.util.ArrayList;

public class Functy {
	
	private int tag;
	public ArrayList<Command> commands;
	public ArrayList<Variable> variables;
	
	public Functy(int tag)
	{
		this.tag = tag;
		commands = new ArrayList<Command>();
		variables = new ArrayList<Variable>();
	}
	
	@Override
	public String toString()
	{
		return "Functy #" + String.valueOf(tag);
	}
	
}
