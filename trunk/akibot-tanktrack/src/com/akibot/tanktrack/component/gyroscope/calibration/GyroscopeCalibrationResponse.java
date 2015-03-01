package com.akibot.tanktrack.component.gyroscope.calibration;

import com.akibot.engine2.message.Response;

public class GyroscopeCalibrationResponse extends Response {
	private static final long serialVersionUID = 1L;

	private double newOffsetX;
	private double newOffsetY;
	private double newOffsetZ;

	public double getNewOffsetX() {
		return newOffsetX;
	}

	public void setNewOffsetX(double newOffsetX) {
		this.newOffsetX = newOffsetX;
	}

	public double getNewOffsetY() {
		return newOffsetY;
	}

	public void setNewOffsetY(double newOffsetY) {
		this.newOffsetY = newOffsetY;
	}

	public double getNewOffsetZ() {
		return newOffsetZ;
	}

	public void setNewOffsetZ(double newOffsetZ) {
		this.newOffsetZ = newOffsetZ;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": new offset = (" + this.newOffsetX + ", " + this.newOffsetY + ", " + this.newOffsetZ + ")";
	}
}
