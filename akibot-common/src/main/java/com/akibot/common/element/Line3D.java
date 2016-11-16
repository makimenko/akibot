package com.akibot.common.element;

public class Line3D implements SimpleGeometryElement {
	private static final long serialVersionUID = 1L;
	private Point3D from;
	private Point3D to;

	public Line3D() {
		// simple
	}

	public Line3D(final Point3D from, final Point3D to) {
		this.from = from;
		this.to = to;
	}

	public Point3D getFrom() {
		return from;
	}

	public void setFrom(final Point3D from) {
		this.from = from;
	}

	public Point3D getTo() {
		return to;
	}

	public void setTo(final Point3D to) {
		this.to = to;
	}

	public Vector3D getVector() {
		final double x = getTo().getX() - getFrom().getX();
		final double y = getTo().getY() - getFrom().getY();
		final double z = getTo().getZ() - getFrom().getZ();
		return new Vector3D(x, y, z);
	}

	@Override
	public String toString() {
		return "Line(" + from + ", " + to + ")";
	}
}
