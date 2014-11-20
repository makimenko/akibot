package com.akibot.tanktrack.component.positioning;

import java.io.Serializable;

public class Vector3d implements Serializable {
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;
	private double z;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
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
		return this.getClass().getName() + "(" + x + "," + y + "," + z + ")";
	}
}
