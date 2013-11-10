package com.iLogo.error;

import com.iLogo.driver.Location;

/**
 * example：undeclared variable 'python'<br>
 */
public class UndeclVarError extends BaseError {

	private String name;

	public UndeclVarError(Location location, String name) {
		super(location);
		this.name = name;
	}

	@Override
	protected String getErrMsg() {
		return "undeclared variable '" + name + "'";
	}

}
