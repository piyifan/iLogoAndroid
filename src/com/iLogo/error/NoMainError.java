package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：the main function not found <br>
 */
public class NoMainError extends BaseError {


	public NoMainError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "the main function not found";
	}

}
