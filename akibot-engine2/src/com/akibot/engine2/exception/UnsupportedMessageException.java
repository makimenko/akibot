package com.akibot.engine2.exception;

public class UnsupportedMessageException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnsupportedMessageException() {
	}

	public UnsupportedMessageException(String message) {
		super(message);
	}

	public UnsupportedMessageException(Throwable cause) {
		super(cause);
	}

	public UnsupportedMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
