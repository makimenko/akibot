package com.akibot.tanktrack.component.gyroscope;

import com.akibot.engine.message.Response;

public class GyroscopeResponse extends Response {
	private double northDegrreesXY;
	private double x;
	private double y;
	private double z;

	public double getNorthDegrreesXY() {
		return northDegrreesXY;
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

	public void setNorthDegrreesXY(double northDegrreesXY) {
		this.northDegrreesXY = northDegrreesXY;
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
		return this.getClass().getName() + ": " + this.getNorthDegrreesXY();
	}

}
