package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.DistanceDetails;

public class WorldDistanceUpdateRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;

	private String nodeName;
	private DistanceDetails distanceDetails;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public DistanceDetails getDistanceDetails() {
		return distanceDetails;
	}

	public void setDistanceDetails(DistanceDetails distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

}
