package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	private double x;
	private double y;
	private double z;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public AkiPoint(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

}
