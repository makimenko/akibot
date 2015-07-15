package com.akibot.engine2.exception;

public class FailedClientConstructorException extends FailedToSendMessageException {
	private static final long serialVersionUID = 1L;

	public FailedClientConstructorException() {
	}

	public FailedClientConstructorException(String message) {
		super(message);
	}

	public FailedClientConstructorException(Throwable cause) {
		super(cause);
	}

	public FailedClientConstructorException(String message, Throwable cause) {
		super(message, cause);
	}

}
