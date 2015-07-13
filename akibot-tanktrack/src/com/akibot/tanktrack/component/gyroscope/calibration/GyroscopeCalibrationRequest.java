package com.akibot.tanktrack.component.gyroscope.calibration;

import com.akibot.engine2.message.Request;

public class GyroscopeCalibrationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private long durationMilliseconds;
	private long sleepMilliseconds;
	private boolean updateConfiguration;

	public GyroscopeCalibrationRequest() {
		this(10000, 100, true);
	}

	public GyroscopeCalibrationRequest(long durationMilliseconds, long sleepMilliseconds, boolean updateConfiguration) {
		this.durationMilliseconds = durationMilliseconds;
		this.sleepMilliseconds = sleepMilliseconds;
		this.updateConfiguration = updateConfiguration;
	}

	public long getSleepMilliseconds() {
		return sleepMilliseconds;
	}

	public void setSleepMilliseconds(long sleepMilliseconds) {
		this.sleepMilliseconds = sleepMilliseconds;
	}

	public long getDurationMilliseconds() {
		return durationMilliseconds;
	}

	public void setDurationMilliseconds(long durationMilliseconds) {
		this.durationMilliseconds = durationMilliseconds;
	}

	public boolean isUpdateConfiguration() {
		return updateConfiguration;
	}

	public void setUpdateConfiguration(boolean updateConfiguration) {
		this.updateConfiguration = updateConfiguration;
	}

}
