package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiAngle implements Serializable {

	private static final long serialVersionUID = 1L;
	private float radians;
	private float degrees;

	public float getRadians() {
		return radians;
	}

	public void setRadians(float radians) {
		this.radians = radians;
	}

	public float getDegrees() {
		return degrees;
	}

	public void setDegrees(float degrees) {
		this.degrees = degrees;
	}

}
