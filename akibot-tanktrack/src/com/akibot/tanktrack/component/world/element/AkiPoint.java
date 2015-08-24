package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	private float x;
	private float y;
	private float z;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public AkiPoint(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

}
