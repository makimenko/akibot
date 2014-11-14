package com.akibot.tanktrack.component.positioning;

public class PositioningVectorRequest extends PositioningRequest {
	private static final long serialVersionUID = 1L;
	private Vector3d vector3d;

	public Vector3d getVector3d() {
		return vector3d;
	}

	public void setVector3d(Vector3d vector3d) {
		this.vector3d = vector3d;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + vector3d;
	}
}
