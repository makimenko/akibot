package com.akibot.tanktrack.component.world.message;

import com.akibot.tanktrack.component.echolocator.MultipleDistanceDetails;

public class WorldMultipleDistanceUpdateRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;

	private String distanceNodeName;
	private String gridNodeName;
	private MultipleDistanceDetails multipleDistanceDetails;

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

	public MultipleDistanceDetails getMultipleDistanceDetails() {
		return multipleDistanceDetails;
	}

	public void setMultipleDistanceDetails(MultipleDistanceDetails multipleDistanceDetails) {
		this.multipleDistanceDetails = multipleDistanceDetails;
	}

}
