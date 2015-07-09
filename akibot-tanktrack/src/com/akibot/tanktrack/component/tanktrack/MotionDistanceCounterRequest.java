package com.akibot.tanktrack.component.tanktrack;

public class MotionDistanceCounterRequest extends MotionRequest {
	private static final long serialVersionUID = 1L;
	private boolean resetAfterResponse = false;

	public boolean isResetAfterResponse() {
		return resetAfterResponse;
	}

	public void setResetAfterResponse(boolean resetAfterResponse) {
		this.resetAfterResponse = resetAfterResponse;
	}

}
