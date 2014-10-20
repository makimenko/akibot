package com.akibot.tanktrack.component.distance;

import com.akibot.engine.message.Response;

public class DistanceResponse extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double meters;

	public double getMeters() {
		return meters;
	}

	public void setMeters(double meters) {
		this.meters = meters;
	}

	@Override
	public String toString() {
		return "Distance is " + meters + " meters";
	}
}
