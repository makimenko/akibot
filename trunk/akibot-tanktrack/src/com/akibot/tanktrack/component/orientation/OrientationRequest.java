package com.akibot.tanktrack.component.orientation;

import com.akibot.engine.message.Request;

public class OrientationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private double northDegrreesXY;
	private double precissionDegrees;
	private double timeoutMillis;

	public double getNorthDegrreesXY() {
		return northDegrreesXY;
	}

	public double getPrecissionDegrees() {
		return precissionDegrees;
	}

	public double getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setNorthDegrreesXY(double northDegrreesXY) {
		this.northDegrreesXY = northDegrreesXY;
	}

	public void setPrecissionDegrees(double precissionDegrees) {
		this.precissionDegrees = precissionDegrees;
	}

	public void setTimeoutMillis(double timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + this.getNorthDegrreesXY() + " / " + this.getPrecissionDegrees();
	}

}
