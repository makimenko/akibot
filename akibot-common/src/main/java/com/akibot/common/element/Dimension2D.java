package com.akibot.common.element;

public class Dimension2D implements SimpleGeometryElement {
	private static final long serialVersionUID = -8083934188721543765L;
	private double x;
	private double y;

	public double getX() {
		return x;
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(final double y) {
		this.y = y;
	}

	public Dimension2D() {
		// Simple
	}

	public Dimension2D(final double x, final double y) {
		this.setX(x);
		this.setY(y);
	}

	public void add(final Dimension2D dimension2d) {
		if (dimension2d != null) {
			setX(getX() + dimension2d.getX());
			setY(getY() + dimension2d.getY());
		}
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append('(').append(getX()).append(',').append(getY()).append(')');
		return buf.toString();
	}

}
