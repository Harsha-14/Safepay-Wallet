package com.cg.safepaywallet.exceptions;

public class UserNameExistsException  extends Exception {
	public UserNameExistsException() {
		super();
	}

	public UserNameExistsException(final String message) {
		super(message);

	}
}
