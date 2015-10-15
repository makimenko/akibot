package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class DistanceDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private Point positionOffset;
	private Angle northAngle;
	private Angle errorAngle;
	private double distanceCm;
	private boolean endObstacle;

	public DistanceDetails(Point positionOffset, Angle northAngle, Angle errorAngle, double distanceCm, boolean endObstacle) {
		this.positionOffset = positionOffset;
		this.northAngle = northAngle;
		this.errorAngle = errorAngle;
		this.distanceCm = distanceCm;
		this.endObstacle = endObstacle;
	}

	public DistanceDetails() {

	}

	public Angle getNorthAngle() {
		return northAngle;
	}

	public void setNorthAngle(Angle northAngle) {
		this.northAngle = northAngle;
	}

	public Angle getErrorAngle() {
		return errorAngle;
	}

	public void setErrorAngle(Angle errorAngle) {
		this.errorAngle = errorAngle;
	}

	public double getDistanceCm() {
		return distanceCm;
	}

	public void setDistanceCm(double distanceCm) {
		this.distanceCm = distanceCm;
	}

	public boolean isEndObstacle() {
		return endObstacle;
	}

	public void setEndObstacle(boolean endObstacle) {
		this.endObstacle = endObstacle;
	}

	public Point getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(Point positionOffset) {
		this.positionOffset = positionOffset;
	}

}
