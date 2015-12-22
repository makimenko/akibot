package sandbox.pi4j.echolocator;

import java.math.BigDecimal;

public class EcholocatorDeviceConfiguration {
	private BigDecimal frequency;
	private BigDecimal frequencyCorrectionFactor;
	private int busNumber;
	private int address;
	private int servoMin = 600;
	private int servoMax = 2500;

	private int distanceTriggerPin;
	private int distanceEchoPin;
	private int distanceTimeout;
	private int sleepBeforeDistance;
	private int servoBasePin;
	private int servoHeadPin;
	private int servoLongTime;
	private int servoStepTime;
	private int distanceCount;

	public BigDecimal getFrequency() {
		return frequency;
	}

	public void setFrequency(BigDecimal frequency) {
		this.frequency = frequency;
	}

	public BigDecimal getFrequencyCorrectionFactor() {
		return frequencyCorrectionFactor;
	}

	public void setFrequencyCorrectionFactor(BigDecimal frequencyCorrectionFactor) {
		this.frequencyCorrectionFactor = frequencyCorrectionFactor;
	}

	public int getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(int busNumber) {
		this.busNumber = busNumber;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getServoMin() {
		return servoMin;
	}

	public void setServoMin(int servoMin) {
		this.servoMin = servoMin;
	}

	public int getServoMax() {
		return servoMax;
	}

	public void setServoMax(int servoMax) {
		this.servoMax = servoMax;
	}

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