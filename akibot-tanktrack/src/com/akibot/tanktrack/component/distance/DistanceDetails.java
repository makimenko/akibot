package com.akibot.tanktrack.component.distance;

import java.io.Serializable;

import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;

public class DistanceDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private Point positionOffset;
	private Angle northAngle;
	private Angle errorAngle;
	private double distanceMm;
	private boolean endObstacle;

	public DistanceDetails(Point positionOffset, Angle northAngle, Angle errorAngle, double distanceMm, boolean endObstacle) {
		this.positionOffset = positionOffset;
		this.northAngle = northAngle;
		this.errorAngle = errorAngle;
		this.distanceMm = distanceMm;
		this.endObstacle = endObstacle;
	}

	public DistanceDetails(double distanceMm, boolean endObstacle) {
		this.positionOffset = new Point(0, 0, 0);
		this.northAngle = new Angle(VectorUtils.gradToRad(0));
		this.errorAngle = new Angle(VectorUtils.gradToRad(0));
		this.distanceMm = distanceMm;
		this.endObstacle = endObstacle;
	}

	public DistanceDetails() {

	}

	public Point getPositionOffset() {
		return positionOffset;
	}

	public void setPositionOffset(Point positionOffset) {
		this.positionOffset = positionOffset;
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

	public double getDistanceMm() {
		return distanceMm;
	}

	public void setDistanceMm(double distanceMm) {
		this.distanceMm = distanceMm;
	}

	public boolean isEndObstacle() {
		return endObstacle;
	}

	public void setEndObstacle(boolean endObstacle) {
		this.endObstacle = endObstacle;
	}

	@Override
	public String toString() {
		return "Distance is: " + getDistanceMm() + "mm = " + (getDistanceMm() / 10) + "cm (endIsObstacle=" + isEndObstacle() + ")";
	}

}
