package com.akibot.tanktrack.component.distance;

import com.akibot.engine.message.Response;

public class DistanceResponse extends Response {

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
