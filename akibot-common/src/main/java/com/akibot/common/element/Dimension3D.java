package com.akibot.common.element;

public class Dimension3D extends Dimension2D {
	private static final long serialVersionUID = -8083934188721543765L;
	private double z;

	public double getZ() {
		return z;
	}

	public void setZ(final double z) {
		this.z = z;
	}

	public Dimension3D() {
		super();
		// simple
	}

	public Dimension3D(final double x, final double y, final double z) {
		super(x, y);
		this.setZ(z);
	}

	public void add(final Dimension3D dimension3d) {
		if (dimension3d != null) {
			setX(getX() + dimension3d.getX());
			setY(getY() + dimension3d.getY());
			setZ(getZ() + dimension3d.getZ());
		}
	}

	public void subtract(final Dimension3D dimension3d) {
		if (dimension3d != null) {
			setX(getX() - dimension3d.getX());
			setY(getY() - dimension3d.getY());
			setZ(getZ() - dimension3d.getZ());
		}
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append('(').append(getX()).append(',').append(getY()).append(',').append(getZ()).append(')');
		return buf.toString();
	}

	public boolean equals(Dimension3D dimension3d, double tolerance) {
		return (dimension3d != null && Math.abs(dimension3d.getX() - getX()) < tolerance
				&& Math.abs(dimension3d.getY() - getY()) < tolerance
				&& Math.abs(dimension3d.getZ() - getZ()) < tolerance);
	}

}
