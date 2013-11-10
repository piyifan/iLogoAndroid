package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：'break' is only allowed inside a loop<br>
 * PA2
 */
public class BreakOutOfLoopError extends BaseError {

	public BreakOutOfLoopError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "'break' is only allowed inside a loop";
	}

}
