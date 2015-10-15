package com.akibot.tanktrack.component.world.exception;

public class WorldException extends Exception {
	private static final long serialVersionUID = 1L;

	public WorldException() {
	}

	public WorldException(String message) {
		super(message);
	}

	public WorldException(Throwable cause) {
		super(cause);
	}

	public WorldException(String message, Throwable cause) {
		super(message, cause);
	}

}
