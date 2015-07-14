package com.akibot.tanktrack.component.gyroscope;

public class GyroscopeConfigurationRequest extends GyroscopeRequest {
	private static final long serialVersionUID = 1L;

	private GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration;

	public GyroscopeOffsetConfiguration getGyroscopeOffsetConfiguration() {
		return gyroscopeOffsetConfiguration;
	}

	public void setGyroscopeOffsetConfiguration(GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration) {
		this.gyroscopeOffsetConfiguration = gyroscopeOffsetConfiguration;
	}

}
