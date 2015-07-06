package com.akibot.tanktrack.component.tanktrack;

public class TimedMotionRequest extends MotionRequest {
	private static final long serialVersionUID = 1L;
	private DirectionType directionType;
	private long milliseconds;

	public TimedMotionRequest() {
		this.directionType = DirectionType.STOP;
	}

	public long getMilliseconds() {
		return milliseconds;
	}

	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}

	public TimedMotionRequest(DirectionType directionType) {
		this.directionType = directionType;
	}

	public DirectionType getDirectionType() {
		return directionType;
	}

	public void setDirectionType(DirectionType directionType) {
		this.directionType = directionType;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + this.directionType + " (" + milliseconds + " ms)";
	}
}
