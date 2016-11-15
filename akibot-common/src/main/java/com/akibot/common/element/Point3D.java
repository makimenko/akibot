package com.akibot.common.element;

public class Point3D extends Dimension3D {
	private static final long serialVersionUID = -6188509139275238540L;

	public Point3D() {
		super();
	}

	public Point3D(final double x, final double y, final double z) {
		super(x, y, z);
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Point3D").append(super.toString());
		return buf.toString();
	}

}
