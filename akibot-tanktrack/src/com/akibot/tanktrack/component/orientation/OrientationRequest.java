package com.akibot.tanktrack.component.orientation;

import com.akibot.engine2.message.Request;

public class OrientationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private double northDegreesXY;
	private double precissionDegrees;
	private double timeoutMillis;

	public double getNorthDegreesXY() {
		return northDegreesXY;
	}

	public double getPrecissionDegrees() {
		return precissionDegrees;
	}

	public double getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setNorthDegreesXY(double northDegreesXY) {
		this.northDegreesXY = northDegreesXY;
	}

	public void setPrecissionDegrees(double precissionDegrees) {
		this.precissionDegrees = precissionDegrees;
	}

	public void setTimeoutMillis(double timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + this.getNorthDegreesXY() + " / " + this.getPrecissionDegrees();
	}

}
