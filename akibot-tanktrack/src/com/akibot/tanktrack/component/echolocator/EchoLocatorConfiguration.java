package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.tanktrack.component.world.element.Angle;

public class EchoLocatorConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private int distanceTriggerPin;
	private int distanceEchoPin;
	private int distanceTimeout;
	private int sleepBeforeDistance;
	private int servoBasePin;
	private int servoHeadPin;
	//
	private int servoLongTime;
	private int servoStepTime;
	private int distanceCount;
	private double distanceMaxMm;
	private Angle errorAngle;

	//
	public int getDistanceTriggerPin() {
		return distanceTriggerPin;
	}

	public void setDistanceTriggerPin(int distanceTriggerPin) {
		this.distanceTriggerPin = distanceTriggerPin;
	}

	public int getDistanceEchoPin() {
		return distanceEchoPin;
	}

	public void setDistanceEchoPin(int distanceEchoPin) {
		this.distanceEchoPin = distanceEchoPin;
	}

	public int getDistanceTimeout() {
		return distanceTimeout;
	}

	public void setDistanceTimeout(int distanceTimeout) {
		this.distanceTimeout = distanceTimeout;
	}

	public int getSleepBeforeDistance() {
		return sleepBeforeDistance;
	}

	public void setSleepBeforeDistance(int sleepBeforeDistance) {
		this.sleepBeforeDistance = sleepBeforeDistance;
	}

	public int getServoBasePin() {
		return servoBasePin;
	}

	public void setServoBasePin(int servoBasePin) {
		this.servoBasePin = servoBasePin;
	}

	public int getServoHeadPin() {
		return servoHeadPin;
	}

	public void setServoHeadPin(int servoHeadPin) {
		this.servoHeadPin = servoHeadPin;
	}

	public int getServoLongTime() {
		return servoLongTime;
	}

	public void setServoLongTime(int servoLongTime) {
		this.servoLongTime = servoLongTime;
	}

	public int getServoStepTime() {
		return servoStepTime;
	}

	public void setServoStepTime(int servoStepTime) {
		this.servoStepTime = servoStepTime;
	}

	public int getDistanceCount() {
		return distanceCount;
	}

	public void setDistanceCount(int distanceCount) {
		this.distanceCount = distanceCount;
	}

	public double getDistanceMaxMm() {
		return distanceMaxMm;
	}

	public void setDistanceMaxMm(double distanceMaxMm) {
		this.distanceMaxMm = distanceMaxMm;
	}

	public Angle getErrorAngle() {
		return errorAngle;
	}

	public void setErrorAngle(Angle errorAngle) {
		this.errorAngle = errorAngle;
	}

}
