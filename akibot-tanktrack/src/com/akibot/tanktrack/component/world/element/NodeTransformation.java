package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class NodeTransformation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Point position;
	private Point scale;
	private Point rotation;

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Point getScale() {
		return scale;
	}

	public void setScale(Point scale) {
		this.scale = scale;
	}

	public Point getRotation() {
		return rotation;
	}

	public void setRotation(Point rotation) {
		this.rotation = rotation;
	}

	public void resetToDefaults() {
		setPosition(new Point(0, 0, 0));
		setRotation(new Point(0, 0, 0));
		setScale(new Point(1, 1, 1));
	}
}
