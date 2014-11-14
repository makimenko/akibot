package com.akibot.tanktrack.component.orientation;

import com.akibot.engine.message.Response;

public class OrientationResponse extends Response {
	private static final long serialVersionUID = 1L;
	private double northDegrreesXY;
	private boolean success;

	public double getNorthDegrreesXY() {
		return northDegrreesXY;
	}

	public void setNorthDegrreesXY(double northDegrreesXY) {
		this.northDegrreesXY = northDegrreesXY;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
