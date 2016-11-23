package com.akibot.app.exception;

public class WorkflowException extends AkiBotApplicationException {
	private static final long serialVersionUID = 1L;

	public WorkflowException() {
		super();
	}

	public WorkflowException(final String message) {
		super(message);
	}

	public WorkflowException(final Throwable cause) {
		super(cause);
	}

	public WorkflowException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
