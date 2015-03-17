package com.akibot.tanktrack.component.servo;

import com.akibot.engine2.message.Request;

public class ServoRequest extends Request {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int value;
	private int microseconds;

	public ServoRequest() {

	}

	public ServoRequest(int value, int microseconds) {
		this.value = value;
		this.microseconds = microseconds;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getMicroseconds() {
		return microseconds;
	}

	public void setMicroseconds(int microseconds) {
		this.microseconds = microseconds;
	}

}
