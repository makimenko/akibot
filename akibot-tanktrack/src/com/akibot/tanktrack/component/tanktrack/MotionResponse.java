package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.message.Response;

public class MotionResponse extends Response {
	private static final long serialVersionUID = 1L;

	private DistanceCounterType distanceCounter = new DistanceCounterType();

	public DistanceCounterType getDistanceCounter() {
		return distanceCounter;
	}

	public void setDistanceCounter(DistanceCounterType distanceCounter) {
		this.distanceCounter = distanceCounter;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + distanceCounter;
	}

}
