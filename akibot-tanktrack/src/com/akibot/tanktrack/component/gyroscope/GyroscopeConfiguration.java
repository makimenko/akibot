package com.akibot.tanktrack.component.gyroscope;

import java.io.Serializable;

public class GyroscopeConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	private double offsetDegrees;
	private double offsetX;
	private double offsetY;
	private double offsetZ;

	public double getOffsetDegrees() {
		return offsetDegrees;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public double getOffsetZ() {
		return offsetZ;
	}

	public void setOffsetDegrees(double offsetDegrees) {
		this.offsetDegrees = offsetDegrees;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	public void setOffsetZ(double offsetZ) {
		this.offsetZ = offsetZ;
	}

	@Override
	public String toString() {
		return "GyroscopeConfiguration (" + offsetX + ", " + offsetY + ", " + offsetZ + "): " + offsetDegrees;
	}
}
