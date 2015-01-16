package com.akibot.tanktrack.component.obstacle;

import com.akibot.engine.message.Response;

public class ObstacleResponse extends Response {
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + this.getX() + ", " + this.getY();
	}

}
