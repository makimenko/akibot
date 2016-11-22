package com.akibot.world.message;

import com.akibot.world.dom.geometry.GridGeometry;

public class GridGeometryFullTransformationEvent extends GridGeometryTransformationEvent {
	private static final long serialVersionUID = -1L;
	private GridGeometry gridGeometry;

	public GridGeometry getGridGeometry() {
		return gridGeometry;
	}

	public void setGridGeometry(GridGeometry gridGeometry) {
		this.gridGeometry = gridGeometry;
	}

}
