package com.akibot.app.exception;

import com.akibot.common.exception.AkiBotException;

public class AkiBotApplicationException extends AkiBotException {
	private static final long serialVersionUID = 1L;

	public AkiBotApplicationException() {
		super();
	}

	public AkiBotApplicationException(final String message) {
		super(message);
	}

	public AkiBotApplicationException(final Throwable cause) {
		super(cause);
	}

	public AkiBotApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
