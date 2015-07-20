package com.akibot.tanktrack.component.gyroscope;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class GyroscopeOffsetConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private double offsetX;
	private double offsetY;
	private double offsetZ;

	public double getOffsetX() {
		return offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public double getOffsetZ() {
		return offsetZ;
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
		return "GyroscopeConfiguration (" + offsetX + ", " + offsetY + ", " + offsetZ + ")";
	}
}
