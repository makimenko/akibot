package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class DistanceDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private AkiPoint positionOffset;
	private AkiAngle northAngle;
	private AkiAngle errorAngle;
	private double distanceCm;
	private boolean endObstacle;

	public DistanceDetails(AkiPoint positionOffset, AkiAngle northAngle, AkiAngle errorAngle, double distanceCm, boolean endObstacle) {
		this.positionOffset = positionOffset;
		this.northAngle = northAngle;
		this.errorAngle = errorAngle;
		this.distanceCm = distanceCm;
		this.endObstacle = endObstacle;
	}

	public DistanceDetails() {

	}

	public AkiAngle getNorthAngle() {
		return northAngle;
	}

	public void setNorthAngle(AkiAngle northAngle) {
		this.northAngle = northAngle;
	}

	public AkiAngle getErrorAngle() {
		return errorAngle;
	}

	public void setErrorAngle(AkiAngle errorAngle) {
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

	public AkiPoint getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(AkiPoint positionOffset) {
		this.positionOffset = positionOffset;
	}

}
