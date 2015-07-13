package com.akibot.tanktrack.component.gyroscope;

public class GyroscopeValueRequest extends GyroscopeRequest {
	private static final long serialVersionUID = 1L;

	private boolean ignoreOffset = false;

	public boolean isIgnoreOffset() {
		return ignoreOffset;
	}

	public void setIgnoreOffset(boolean ignoreOffset) {
		this.ignoreOffset = ignoreOffset;
	}

}
