package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：integer literal 112233445566778899 is too large<br>
 */
public class IntTooLargeError extends BaseError {

	private String val;

	public IntTooLargeError(Location location, String val) {
		super(location);
		this.val = val;
	}

	@Override
	protected String getErrMsg() {
		return "integer literal " + val + " is too large";
	}

}
