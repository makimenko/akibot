package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.message.Response;

public class EchoLocatorResponse extends Response {
	private static final long serialVersionUID = 1L;

	private float[] echoLocatorResult;

	public float[] getEchoLocatorResult() {
		return echoLocatorResult;
	}

	public void setEchoLocatorResult(float[] echoLocatorResult) {
		this.echoLocatorResult = echoLocatorResult;
	}

}
