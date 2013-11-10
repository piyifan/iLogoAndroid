package com.iLogo.iCode;

public class Label {

	public int pos;
	
	public Label() {
		this.pos = -1;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	@Override
	public String toString()
	{
		return "^" + String.valueOf(pos);
	}
}
