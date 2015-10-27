package com.akibot.engine2.exception;

public class WorkflowException extends Exception {
	private static final long serialVersionUID = 1L;

	public WorkflowException() {
	}

	public WorkflowException(String message) {
		super(message);
	}

	public WorkflowException(Throwable cause) {
		super(cause);
	}

	public WorkflowException(String message, Throwable cause) {
		super(message, cause);
	}

}
