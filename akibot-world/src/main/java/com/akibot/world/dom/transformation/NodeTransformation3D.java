package com.akibot.world.dom.transformation;

import com.akibot.common.element.Vector3D;

public class NodeTransformation3D implements Transformation {
	private static final long serialVersionUID = 1L;
	private Vector3D position;
	private Vector3D scale;
	private Vector3D rotation;

	public Vector3D getPosition() {
		return position;
	}

	public void setPosition(final Vector3D position) {
		this.position = position;
	}

	public Vector3D getScale() {
		return scale;
	}

	public void setScale(final Vector3D scale) {
		this.scale = scale;
	}

	public Vector3D getRotation() {
		return rotation;
	}

	public void setRotation(final Vector3D rotation) {
		this.rotation = rotation;
	}

	public void resetToDefaults() {
		setPosition(new Vector3D(0, 0, 0));
		setRotation(new Vector3D(0, 0, 0));
		setScale(new Vector3D(1, 1, 1));
	}
}
