package com.akibot.engine2.exception;

public class FailedToSendMessageException extends Exception {
	private static final long serialVersionUID = 1L;

	public FailedToSendMessageException() {
	}

	public FailedToSendMessageException(String message) {
		super(message);
	}

	public FailedToSendMessageException(Throwable cause) {
		super(cause);
	}

	public FailedToSendMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
