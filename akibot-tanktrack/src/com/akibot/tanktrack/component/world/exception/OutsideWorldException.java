package com.akibot.tanktrack.component.world.exception;

public class OutsideWorldException extends WorldException {
	private static final long serialVersionUID = 1L;

	public OutsideWorldException() {
	}

	public OutsideWorldException(String message) {
		super(message);
	}

	public OutsideWorldException(Throwable cause) {
		super(cause);
	}

	public OutsideWorldException(String message, Throwable cause) {
		super(message, cause);
	}

}
