package com.akibot.tanktrack.component.distance;

import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.tanktrack.component.world.element.Angle;

public class DistanceMeterConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private int triggerPin;
	private int echoPin;
	private long timeoutMicroseconds;
	private long maxDistanceMm;
	private Angle errorAngle;

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

	public long getTimeoutMicroseconds() {
		return timeoutMicroseconds;
	}

	public void setTimeoutMicroseconds(long timeoutMicroseconds) {
		this.timeoutMicroseconds = timeoutMicroseconds;
	}

	public long getMaxDistanceMm() {
		return maxDistanceMm;
	}

	public void setMaxDistanceMm(long maxDistanceMm) {
		this.maxDistanceMm = maxDistanceMm;
	}

	public Angle getErrorAngle() {
		return errorAngle;
	}

	public void setErrorAngle(Angle errorAngle) {
		this.errorAngle = errorAngle;
	}

}
