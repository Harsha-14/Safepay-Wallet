package com.cg.safepaywallet.exceptions;

import java.util.List;

public class SignUpExceptionResponse {
	
	private String errorMessage;
	private List<String> details;

	public SignUpExceptionResponse(String errorMessage, List<String> details) {
		super();
		this.errorMessage = errorMessage;
		this.details = details;
	}
	public SignUpExceptionResponse() {
		super();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(final List<String> details) {
		this.details = details;
	}
}
