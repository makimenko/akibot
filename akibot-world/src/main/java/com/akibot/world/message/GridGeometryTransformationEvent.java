package com.akibot.world.message;

public class GridGeometryTransformationEvent extends WorldRealtimeEvent {
	private static final long serialVersionUID = 3152756094201080086L;
	private String gridNodeName;

	public String getGridNodeName() {
		return gridNodeName;
	}

	public void setGridNodeName(String gridNodeName) {
		this.gridNodeName = gridNodeName;
	}

}
