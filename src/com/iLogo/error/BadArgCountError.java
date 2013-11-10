package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：function 'gotoMars' expects 1 argument(s) but 3 given<br>
 */
public class BadArgCountError extends BaseError {

	private String method;

	private int expect;

	private int count;

	public BadArgCountError(Location location, String method, int expect,
			int count) {
		super(location);
		this.method = method;
		this.expect = expect;
		this.count = count;
	}

	@Override
	protected String getErrMsg() {
		return "function '" + method + "' expects " + expect
				+ " argument(s) but " + count + " given";
	}
}
