package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class Angle implements Serializable {

	private static final long serialVersionUID = 1L;
	private double radians;

	public Angle() {

	}

	public Angle(double radians) {
		this.radians = radians;
	}

	public double getRadians() {
		return radians;
	}

	public void setRadians(double radians) {
		this.radians = radians;
	}

	public double getDegrees() {
		return Math.toDegrees(radians);
	}

	public void setDegrees(double degrees) {
		this.radians = Math.toRadians(degrees);
	}

	public Angle getNegativeAngle() {
		return new Angle(-radians);
	}

}
