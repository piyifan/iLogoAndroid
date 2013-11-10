package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：test expression must have bool type<br>
 */
public class BadTestExpr extends BaseError {

	public BadTestExpr(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "test expression must have bool type";
	}

}
