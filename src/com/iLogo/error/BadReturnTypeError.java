package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：incompatible return: int[] given, int expected<br>
 * PA2
 */
public class BadReturnTypeError extends BaseError {

	private String expect;

	private String given;

	public BadReturnTypeError(Location location, String expect, String given) {
		super(location);
		this.expect = expect;
		this.given = given;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible return: " + given + " given, " + expect
				+ " expected";
	}
}
