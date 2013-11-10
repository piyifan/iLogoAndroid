package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：function 'money' not found <br>
 */
public class FuncNotFoundError extends BaseError {

	private String name;

	public FuncNotFoundError(Location location, String name) {
		super(location);
		this.name = name;
	}

	@Override
	protected String getErrMsg() {
		return "field '" + name + "' not found";
	}

}
