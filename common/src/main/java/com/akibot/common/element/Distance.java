package com.akibot.common.element;

public class Distance implements SimpleGeometryElement {
	private static final long serialVersionUID = -297960825250634298L;

	private double distanceMm;
	private Angle errorAngle;
	private boolean endObstacle;

	public Distance() {
		this.errorAngle = new Angle();
		this.endObstacle = false;
	}

	public Distance(double distanceMm) {
		this();
		this.distanceMm = distanceMm;
	}

	public double getDistanceMm() {
		return distanceMm;
	}

	public void setDistanceMm(double distanceMm) {
		this.distanceMm = distanceMm;
	}

	public Angle getErrorAngle() {
		return errorAngle;
	}

	public void setErrorAngle(Angle errorAngle) {
		this.errorAngle = errorAngle;
	}

	public boolean isEndObstacle() {
		return endObstacle;
	}

	public void setEndObstacle(boolean endObstacle) {
		this.endObstacle = endObstacle;
	}

}
