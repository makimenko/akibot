package com.akibot.tanktrack.component.orientation;

import com.akibot.engine2.message.Response;

public class OrientationResponse extends Response {
	private static final long serialVersionUID = 1L;
	private double northDegreesXY;
	private boolean success;

	public double getNorthDegreesXY() {
		return northDegreesXY;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setNorthDegreesXY(double northDegreesXY) {
		this.northDegreesXY = northDegreesXY;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + isSuccess() + " (" + northDegreesXY + ")";
	}

}
