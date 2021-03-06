package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：incompatible argument 3: int given, bool expected<br>
 * 3表示发生错误的是第三个参数<br>
 */
public class BadArgTypeError extends BaseError {

	private int count;

	private String given;

	private String expect;

	public BadArgTypeError(Location location, int count, String given,
			String expect) {
		super(location);
		this.count = count;
		this.given = given;
		this.expect = expect;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible argument " + count + ": " + given + " given, "
				+ expect + " expected";
	}

}
