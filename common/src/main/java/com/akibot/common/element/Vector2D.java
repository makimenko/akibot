package com.akibot.common.element;

public class Vector2D extends Dimension2D {
	private static final long serialVersionUID = -1779094708754719993L;

	public Vector2D() {
		super();
	}

	public Vector2D(final double x, final double y) {
		super(x, y);
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Vector2D").append(super.toString());
		return buf.toString();
	}

}
