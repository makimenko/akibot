package com.akibot.tanktrack.component.gyroscope;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class GyroscopeConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration;
	private int busNumber;
	private int deviceAddress;

	public GyroscopeOffsetConfiguration getGyroscopeOffsetConfiguration() {
		return gyroscopeOffsetConfiguration;
	}

	public void setGyroscopeOffsetConfiguration(GyroscopeOffsetConfiguration gyroscopeOffsetConfiguration) {
		this.gyroscopeOffsetConfiguration = gyroscopeOffsetConfiguration;
	}

	public int getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(int busNumber) {
		this.busNumber = busNumber;
	}

	public int getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(int deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

}
