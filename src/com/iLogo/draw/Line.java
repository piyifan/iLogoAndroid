package com.iLogo.draw;


public class Line {
	public float x0, y0, x1, y1;
	
	public Line(double x0, double y0, double x1, double y1) {
		this.x0 = (float) x0;
		this.y0 = (float) y0;
		this.x1 = (float) x1;
		this.y1 = (float) y1;
	}
	
	@Override 
	public String toString() {
		return "( " + String.valueOf(x0) + ", " + String.valueOf(y0) + " )"
		+ " -> " +"( " + String.valueOf(x1) + ", " + String.valueOf(y1) + " )";
	}
}

