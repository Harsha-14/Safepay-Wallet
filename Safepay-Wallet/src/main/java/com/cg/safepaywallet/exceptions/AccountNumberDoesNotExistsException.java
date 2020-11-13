package com.cg.safepaywallet.exceptions;

public class AccountNumberDoesNotExistsException extends Exception {

	private static final long serialVersionUID = -9079454849611061074L;

	public AccountNumberDoesNotExistsException() {
		super();
	}

	public AccountNumberDoesNotExistsException(final String message) {
		super(message);

	} 


}
