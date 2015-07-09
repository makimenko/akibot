package com.akibot.engine2.exception;

public class FailedToStartException extends Exception {
	private static final long serialVersionUID = 1L;

	public FailedToStartException() {
	}

	public FailedToStartException(String message) {
		super(message);
	}

	public FailedToStartException(Throwable cause) {
		super(cause);
	}

	public FailedToStartException(String message, Throwable cause) {
		super(message, cause);
	}

}
