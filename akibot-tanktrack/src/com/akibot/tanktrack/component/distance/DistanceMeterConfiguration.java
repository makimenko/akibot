package com.akibot.tanktrack.component.distance;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class DistanceMeterConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private int triggerPin;
	private int echoPin;
	private int timeoutMicroseconds;

	public int getTriggerPin() {
		return triggerPin;
	}

	public void setTriggerPin(int triggerPin) {
		this.triggerPin = triggerPin;
	}

	public int getEchoPin() {
		return echoPin;
	}

	public void setEchoPin(int echoPin) {
		this.echoPin = echoPin;
	}

	public int getTimeoutMicroseconds() {
		return timeoutMicroseconds;
	}

	public void setTimeoutMicroseconds(int timeoutMicroseconds) {
		this.timeoutMicroseconds = timeoutMicroseconds;
	}

}
