package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.message.Request;

public class EchoLocatorRequest extends Request {
	private static final long serialVersionUID = 1L;
	private int servoBaseFrom;
	private int servoBaseTo;
	private int servoBaseStep;
	private int servoHeadNormal;
	private boolean trustToLastPosition;

	public int getServoBaseFrom() {
		return servoBaseFrom;
	}

	public void setServoBaseFrom(int servoBaseFrom) {
		this.servoBaseFrom = servoBaseFrom;
	}

	public int getServoBaseTo() {
		return servoBaseTo;
	}

	public void setServoBaseTo(int servoBaseTo) {
		this.servoBaseTo = servoBaseTo;
	}

	public int getServoBaseStep() {
		return servoBaseStep;
	}

	public void setServoBaseStep(int servoBaseStep) {
		this.servoBaseStep = servoBaseStep;
	}

	public int getServoHeadNormal() {
		return servoHeadNormal;
	}

	public void setServoHeadNormal(int servoHeadNormal) {
		this.servoHeadNormal = servoHeadNormal;
	}

	public boolean isTrustToLastPosition() {
		return trustToLastPosition;
	}

	public void setTrustToLastPosition(boolean trustToLastPosition) {
		this.trustToLastPosition = trustToLastPosition;
	}

}
