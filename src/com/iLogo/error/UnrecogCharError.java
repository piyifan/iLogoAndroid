package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：unrecognized char: '@'<br>
 * PA1
 */
public class UnrecogCharError extends BaseError {

	private char c;

	public UnrecogCharError(Location location, char c) {
		super(location);
		this.c = c;
	}

	@Override
	protected String getErrMsg() {
		return "unrecognized character '" + c + "'";
	}
}
