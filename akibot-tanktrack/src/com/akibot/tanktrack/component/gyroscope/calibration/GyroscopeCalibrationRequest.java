package com.akibot.tanktrack.component.gyroscope.calibration;

import com.akibot.engine.message.Request;

public class GyroscopeCalibrationRequest extends Request {
	private static final long serialVersionUID = 1L;

	private long durationMilliseconds;
	private long sleepMilliseconds;
	private boolean resetOffset;
	private boolean updateConfiguration;

	public GyroscopeCalibrationRequest() {
		this(10000, 100, true, true);
	}

	public GyroscopeCalibrationRequest(long durationMilliseconds, long sleepMilliseconds, boolean resetOffset, boolean updateConfiguration) {
		this.durationMilliseconds = durationMilliseconds;
		this.sleepMilliseconds = sleepMilliseconds;
		this.resetOffset = resetOffset;
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

	public boolean isResetOffset() {
		return resetOffset;
	}

	public void setResetOffset(boolean resetOffset) {
		this.resetOffset = resetOffset;
	}

	public boolean isUpdateConfiguration() {
		return updateConfiguration;
	}

	public void setUpdateConfiguration(boolean updateConfiguration) {
		this.updateConfiguration = updateConfiguration;
	}

}
