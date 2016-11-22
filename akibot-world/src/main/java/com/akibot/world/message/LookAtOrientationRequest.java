package com.akibot.world.message;

import com.akibot.common.element.Point3D;

public class LookAtOrientationRequest extends WorldCommandRequest {
	private static final long serialVersionUID = 2877239967817328536L;

	private Point3D lookAt;

	public Point3D getLookAt() {
		return lookAt;
	}

	public void setLookAt(Point3D lookAt) {
		this.lookAt = lookAt;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(100);
		buf.append("LookAtOrientationRequest(").append(lookAt).append(")");
		return buf.toString();
	}

}
