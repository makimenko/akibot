package com.akibot.common.utils;

public class CommonUtilsException extends Exception {
	private static final long serialVersionUID = -5222817599443528467L;

	public CommonUtilsException() {
		super();
	}

	public CommonUtilsException(final String message) {
		super(message);
	}

	public CommonUtilsException(final Throwable cause) {
		super(cause);
	}

	public CommonUtilsException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
