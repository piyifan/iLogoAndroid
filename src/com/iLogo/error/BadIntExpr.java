package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：this expression must have int type<br>
 */
public class BadIntExpr extends BaseError {

	public BadIntExpr(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "this expression must have int type";
	}

}
