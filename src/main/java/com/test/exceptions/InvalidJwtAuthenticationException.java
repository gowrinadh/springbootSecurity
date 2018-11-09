package com.test.exceptions;

public class InvalidJwtAuthenticationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJwtAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
