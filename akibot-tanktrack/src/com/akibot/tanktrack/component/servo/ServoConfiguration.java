package com.akibot.tanktrack.component.servo;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class ServoConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private int servoPin;
	private int initialValue;
	private int pwmRange;
	private int divisor;

	public int getServoPin() {
		return servoPin;
	}

	public void setServoPin(int servoPin) {
		this.servoPin = servoPin;
	}

	public int getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(int initialValue) {
		this.initialValue = initialValue;
	}

	public int getPwmRange() {
		return pwmRange;
	}

	public void setPwmRange(int pwmRange) {
		this.pwmRange = pwmRange;
	}

	public int getDivisor() {
		return divisor;
	}

	public void setDivisor(int divisor) {
		this.divisor = divisor;
	}

}
