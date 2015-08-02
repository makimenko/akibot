package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class TankTrackConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;

	private int rightIApin;
	private int rightIBpin;
	private int leftIApin;
	private int leftIBpin;
	private int leftSpeedPin;
	private int rightSpeedPin;

	public int getRightIApin() {
		return rightIApin;
	}

	public void setRightIApin(int rightIApin) {
		this.rightIApin = rightIApin;
	}

	public int getRightIBpin() {
		return rightIBpin;
	}

	public void setRightIBpin(int rightIBpin) {
		this.rightIBpin = rightIBpin;
	}

	public int getLeftIApin() {
		return leftIApin;
	}

	public void setLeftIApin(int leftIApin) {
		this.leftIApin = leftIApin;
	}

	public int getLeftIBpin() {
		return leftIBpin;
	}

	public void setLeftIBpin(int leftIBpin) {
		this.leftIBpin = leftIBpin;
	}

	public int getLeftSpeedPin() {
		return leftSpeedPin;
	}

	public void setLeftSpeedPin(int leftSpeedPin) {
		this.leftSpeedPin = leftSpeedPin;
	}

	public int getRightSpeedPin() {
		return rightSpeedPin;
	}

	public void setRightSpeedPin(int rightSpeedPin) {
		this.rightSpeedPin = rightSpeedPin;
	}

}
