package com.akibot.tanktrack.component.tanktrack;

public class StickMotionRequest extends MotionRequest {
	private static final long serialVersionUID = 1L;
	private DirectionType directionType;

	public StickMotionRequest() {
		this.directionType = DirectionType.STOP;
	}

	public StickMotionRequest(DirectionType directionType) {
		this.directionType = directionType;
	}

	public DirectionType getDirectionType() {
		return directionType;
	}

	public void setDirectionType(DirectionType directionType) {
		this.directionType = directionType;
	}

}
