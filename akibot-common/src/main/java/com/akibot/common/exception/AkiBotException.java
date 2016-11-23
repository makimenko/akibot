package com.akibot.common.exception;


public class AkiBotException extends Exception {
	private static final long serialVersionUID = 1L;

	public AkiBotException() {
		super();
	}

	public AkiBotException(final String message) {
		super(message);
	}

	public AkiBotException(final Throwable cause) {
		super(cause);
	}

	public AkiBotException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

