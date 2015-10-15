package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiNodeTransformation implements Serializable {

	private static final long serialVersionUID = 1L;

	private AkiPoint position;
	private AkiPoint scale;
	private AkiPoint rotation;

	public AkiPoint getPosition() {
		return position;
	}

	public void setPosition(AkiPoint position) {
		this.position = position;
	}

	public AkiPoint getScale() {
		return scale;
	}

	public void setScale(AkiPoint scale) {
		this.scale = scale;
	}

	public AkiPoint getRotation() {
		return rotation;
	}

	public void setRotation(AkiPoint rotation) {
		this.rotation = rotation;
	}

	public void resetToDefaults() {
		setPosition(new AkiPoint(0, 0, 0));
		setRotation(new AkiPoint(0, 0, 0));
		setScale(new AkiPoint(1, 1, 1));
	}
}
