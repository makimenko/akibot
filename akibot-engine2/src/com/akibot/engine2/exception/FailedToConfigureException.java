package com.akibot.engine2.exception;

public class FailedToConfigureException extends Exception {
	private static final long serialVersionUID = 1L;

	public FailedToConfigureException() {
	}

	public FailedToConfigureException(String message) {
		super(message);
	}

	public FailedToConfigureException(Throwable cause) {
		super(cause);
	}

	public FailedToConfigureException(String message, Throwable cause) {
		super(message, cause);
	}

}
