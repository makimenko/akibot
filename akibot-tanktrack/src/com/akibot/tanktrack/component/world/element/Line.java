package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class Line implements Serializable {
	private static final long serialVersionUID = 1L;
	private Point from;
	private Point to;

	public Line() {

	}

	public Line(Point from, Point to) {
		this.from = from;
		this.to = to;
	}

	public Point getFrom() {
		return from;
	}

	public void setFrom(Point from) {
		this.from = from;
	}

	public Point getTo() {
		return to;
	}

	public void setTo(Point to) {
		this.to = to;
	}

	public Point getVector() {
		double x = getTo().getX() - getFrom().getX();
		double y = getTo().getY() - getFrom().getY();
		double z = getTo().getZ() - getFrom().getZ();
		return new Point(x, y, z);
	}
}
