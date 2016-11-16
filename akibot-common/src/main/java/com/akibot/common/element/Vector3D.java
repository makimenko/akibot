package com.akibot.common.element;

public class Vector3D extends Dimension3D {
	private static final long serialVersionUID = -1779094708754719993L;

	public Vector3D() {
		super();
	}

	public Vector3D(final double x, final double y, final double z) {
		super(x, y, z);
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Vector3D").append(super.toString());
		return buf.toString();
	}

}
