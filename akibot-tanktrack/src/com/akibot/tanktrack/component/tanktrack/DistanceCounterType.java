package com.akibot.tanktrack.component.tanktrack;

import java.io.Serializable;

public class DistanceCounterType implements Serializable {
	private long leftDistanceCounter;
	private long rightDistanceCounter;

	public long getLeftDistanceCounter() {
		return leftDistanceCounter;
	}

	public void setLeftDistanceCounter(long leftDistanceCounter) {
		this.leftDistanceCounter = leftDistanceCounter;
	}

	public long getRightDistanceCounter() {
		return rightDistanceCounter;
	}

	public void setRightDistanceCounter(long rightDistanceCounter) {
		this.rightDistanceCounter = rightDistanceCounter;
	}

	@Override
	public String toString() {
		return "Distance L=" + leftDistanceCounter + ", R=" + rightDistanceCounter;
	}
}
