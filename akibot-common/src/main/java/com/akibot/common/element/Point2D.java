package com.akibot.common.element;

public class Point2D extends Dimension2D {
	private static final long serialVersionUID = 1L;

	public Point2D() {
		super();
	}

	public Point2D(final double x, final double y) {
		super(x, y);
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Point2D").append(super.toString());
		return buf.toString();
	}

}
