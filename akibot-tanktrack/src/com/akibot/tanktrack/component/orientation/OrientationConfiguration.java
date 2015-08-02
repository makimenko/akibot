package com.akibot.tanktrack.component.orientation;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class OrientationConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private String gyroscopeName;
	private String tankTrackName;
	private int syncRequestTimeout;
	private long stepMillis;
	private long easyMillis;
	private long easyDegrees;

	public String getGyroscopeName() {
		return gyroscopeName;
	}

	public void setGyroscopeName(String gyroscopeName) {
		this.gyroscopeName = gyroscopeName;
	}

	public String getTankTrackName() {
		return tankTrackName;
	}

	public void setTankTrackName(String tankTrackName) {
		this.tankTrackName = tankTrackName;
	}

	public int getSyncRequestTimeout() {
		return syncRequestTimeout;
	}

	public void setSyncRequestTimeout(int syncRequestTimeout) {
		this.syncRequestTimeout = syncRequestTimeout;
	}

	public long getStepMillis() {
		return stepMillis;
	}

	public void setStepMillis(long stepMillis) {
		this.stepMillis = stepMillis;
	}

	public long getEasyMillis() {
		return easyMillis;
	}

	public void setEasyMillis(long easyMillis) {
		this.easyMillis = easyMillis;
	}

	public long getEasyDegrees() {
		return easyDegrees;
	}

	public void setEasyDegrees(long easyDegrees) {
		this.easyDegrees = easyDegrees;
	}

}
