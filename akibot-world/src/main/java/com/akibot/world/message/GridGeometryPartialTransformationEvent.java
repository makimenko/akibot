package com.akibot.world.message;

import java.util.ArrayList;
import java.util.List;

import com.akibot.world.dom.geometry.GridGeometryCellValue;

public class GridGeometryPartialTransformationEvent extends GridGeometryTransformationEvent {
	private static final long serialVersionUID = 3152756094201080086L;

	private List<GridGeometryCellValue> changeLog;

	public GridGeometryPartialTransformationEvent() {
		this.changeLog = new ArrayList<GridGeometryCellValue>();
	}

	public List<GridGeometryCellValue> getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(List<GridGeometryCellValue> changeLog) {
		this.changeLog = changeLog;
	}

}
