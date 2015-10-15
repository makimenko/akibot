package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.world.element.DistanceDetails;

public class WorldDistanceUpdateRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;

	private String distanceNodeName;
	private String gridNodeName;
	private DistanceDetails distanceDetails;

	public DistanceDetails getDistanceDetails() {
		return distanceDetails;
	}

	public void setDistanceDetails(DistanceDetails distanceDetails) {
		this.distanceDetails = distanceDetails;
	}

	public String getGridNodeName() {
		return gridNodeName;
	}

	public void setGridNodeName(String gridNodeName) {
		this.gridNodeName = gridNodeName;
	}

	public String getDistanceNodeName() {
		return distanceNodeName;
	}

	public void setDistanceNodeName(String distanceNodeName) {
		this.distanceNodeName = distanceNodeName;
	}

}
