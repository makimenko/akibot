package com.akibot.tanktrack.component.gyroscope;

import com.akibot.engine2.message.Response;

public class GyroscopeResponse extends Response {
	private static final long serialVersionUID = 1L;
	private double northDegreesXY;
	private double x;
	private double y;
	private double z;

	public double getNorthDegreesXY() {
		return northDegreesXY;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setNorthDegreesXY(double northDegreesXY) {
		this.northDegreesXY = northDegreesXY;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + this.getNorthDegreesXY();
	}

}
