package com.akibot.tanktrack.component.servo;

import com.akibot.engine2.message.Response;

public class ServoResponse extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double mm;

	public double getMm() {
		return mm;
	}

	public void setMm(double mm) {
		this.mm = mm;
	}

	@Override
	public String toString() {
		return "Distance is " + mm + " mm";
	}
}
