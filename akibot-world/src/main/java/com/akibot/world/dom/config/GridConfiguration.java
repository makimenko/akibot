package com.akibot.world.dom.config;

import com.akibot.common.element.Vector3D;

public class GridConfiguration implements WorldConfiguration {
	private static final long serialVersionUID = 1L;
	private int cellCountX;
	private int cellCountY;
	private double cellSizeMm;
	private int maxObstacleCount;
	private Vector3D offsetVector;
	private int unknownValue;
	private int emptyValue;

	public GridConfiguration(int cellCountX, int cellCountY, float cellSize, int maxObstacleCount) {
		this(cellCountX, cellCountY, cellSize, maxObstacleCount, new Vector3D(0, 0, 0));
	}

	public GridConfiguration(int cellCountX, int cellCountY, float cellSizeMm, int maxObstacleCount,
			Vector3D offsetVector) {
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
		this.cellSizeMm = cellSizeMm;
		this.maxObstacleCount = maxObstacleCount;
		this.offsetVector = offsetVector;
		this.unknownValue = -1;
		this.emptyValue = 0;
	}

	public int getCellCountX() {
		return cellCountX;
	}

	public void setCellCountX(int cellCountX) {
		this.cellCountX = cellCountX;
	}

	public int getCellCountY() {
		return cellCountY;
	}

	public void setCellCountY(int cellCountY) {
		this.cellCountY = cellCountY;
	}

	public double getCellSizeMm() {
		return cellSizeMm;
	}

	public void setCellSizeMm(double cellSizeMm) {
		this.cellSizeMm = cellSizeMm;
	}

	public int getMaxObstacleCount() {
		return maxObstacleCount;
	}

	public void setMaxObstacleCount(int maxObstacleCount) {
		this.maxObstacleCount = maxObstacleCount;
	}

	public Vector3D getOffsetVector() {
		return offsetVector;
	}

	public void setOffsetVector(Vector3D offsetVector) {
		this.offsetVector = offsetVector;
	}

	public int getUnknownValue() {
		return unknownValue;
	}

	public void setUnknownValue(int unknownValue) {
		this.unknownValue = unknownValue;
	}

	public int getEmptyValue() {
		return emptyValue;
	}

	public void setEmptyValue(int emptyValue) {
		this.emptyValue = emptyValue;
	}

}
