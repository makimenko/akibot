package com.akibot.tanktrack.component.world.message;

public class WorldGridResetRequest extends WorldUpdateRequest {
	private static final long serialVersionUID = 1L;
	private String gridNodeName;

	public String getGridNodeName() {
		return gridNodeName;
	}

	public void setGridNodeName(String gridNodeName) {
		this.gridNodeName = gridNodeName;
	}

}
