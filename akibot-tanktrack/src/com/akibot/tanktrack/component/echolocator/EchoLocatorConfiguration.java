package com.akibot.tanktrack.component.echolocator;

import java.io.Serializable;

public class EchoLocatorConfiguration implements Serializable {
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

}
