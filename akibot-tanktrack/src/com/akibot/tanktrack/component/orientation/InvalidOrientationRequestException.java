package com.akibot.tanktrack.component.orientation;

public class InvalidOrientationRequestException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidOrientationRequestException() {
	}

	public InvalidOrientationRequestException(String message) {
		super(message);
	}

	public InvalidOrientationRequestException(Throwable cause) {
		super(cause);
	}

	public InvalidOrientationRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
