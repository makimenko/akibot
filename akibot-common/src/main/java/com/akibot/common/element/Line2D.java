package com.akibot.common.element;

public class Line2D implements SimpleGeometryElement {
	private static final long serialVersionUID = 1L;
	private Point2D from;
	private Point2D to;

	public Line2D() {
		// Simple
	}

	public Line2D(final Point2D from, final Point2D to) {
		this.from = from;
		this.to = to;
	}

	public Point2D getFrom() {
		return from;
	}

	public void setFrom(final Point2D from) {
		this.from = from;
	}

	public Point2D getTo() {
		return to;
	}

	public void setTo(final Point2D to) {
		this.to = to;
	}

	public Vector2D getVector() {
		final double x = getTo().getX() - getFrom().getX();
		final double y = getTo().getY() - getFrom().getY();
		return new Vector2D(x, y);
	}

	@Override
	public String toString() {
		return "Line(" + from + " - " + to + ")";
	}
}
