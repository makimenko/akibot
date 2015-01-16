package com.akibot.tanktrack.component.obstacle;

import com.akibot.engine.message.Request;

public class ObstacleRequest extends Request {
	private static final long serialVersionUID = 1L;

	private String gyroscopeName;
	private String distanceName;

	public ObstacleRequest() {
		this(null, null);
	}

	public ObstacleRequest(String gyroscopeName, String distanceName) {
		this.gyroscopeName = gyroscopeName;
		this.distanceName = distanceName;
	}

	public String getGyroscopeName() {
		return gyroscopeName;
	}

	public void setGyroscopeName(String gyroscopeName) {
		this.gyroscopeName = gyroscopeName;
	}

	public String getDistanceName() {
		return distanceName;
	}

	public void setDistanceName(String distanceName) {
		this.distanceName = distanceName;
	}

}
