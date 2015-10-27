package com.akibot.tanktrack.component.distance;

import com.akibot.engine2.message.Response;

public class DistanceResponse extends Response {
	private static final long serialVersionUID = 1L;

	private DistanceDetails distanceDetails;

	public DistanceResponse() {
	}

	public DistanceResponse(DistanceDetails distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

	public DistanceDetails getDistanceDetails() {
		return distanceDetails;
	}

	public void setDistanceDetails(DistanceDetails distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

	@Override
	public String toString() {
		if (getDistanceDetails() != null) {
			return getDistanceDetails().toString();
		} else {
			return "Distance is unknown";
		}
	}
}
