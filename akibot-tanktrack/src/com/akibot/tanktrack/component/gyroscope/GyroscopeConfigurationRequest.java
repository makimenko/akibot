package com.akibot.tanktrack.component.gyroscope;

public class GyroscopeConfigurationRequest extends GyroscopeRequest {
	private static final long serialVersionUID = 1L;

	private GyroscopeConfiguration gyroscopeConfiguration;

	public GyroscopeConfiguration getGyroscopeConfiguration() {
		return gyroscopeConfiguration;
	}

	public void setGyroscopeConfiguration(GyroscopeConfiguration gyroscopeConfiguration) {
		this.gyroscopeConfiguration = gyroscopeConfiguration;
	}

}
