package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：incompatible operand: - int[]<br>
 * PA2
 */
public class IncompatUnOpError extends BaseError {

	private String op;

	private String expr;

	public IncompatUnOpError(Location location, String op, String expr) {
		super(location);
		this.op = op;
		this.expr = expr;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible operand: " + op + " " + expr;
	}

}
