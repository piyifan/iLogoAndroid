package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：incompatible operands: int + bool<br>
 */
public class IncompatBinOpError extends BaseError {

	private String left;

	private String right;

	private String op;

	public IncompatBinOpError(Location location, String left, String op,
			String right) {
		super(location);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible operands: " + left + " " + op + " " + right;
	}

}
