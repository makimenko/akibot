package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class GridConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	private int cellCountX;
	private int cellCountY;
	private double cellSizeMm;
	private int maxObstacleCount;
	private Point offsetMm;

	public GridConfiguration() {

	}

	public GridConfiguration(int cellCountX, int cellCountY, float cellSize, int maxObstacleCount) {
		this(cellCountX, cellCountY, cellSize, maxObstacleCount, new Point(0, 0, 0));
	}

	public GridConfiguration(int cellCountX, int cellCountY, float cellSizeMm, int maxObstacleCount, Point offsetMm) {
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
		this.cellSizeMm = cellSizeMm;
		this.maxObstacleCount = maxObstacleCount;
		this.offsetMm = offsetMm;
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

	public Point getOffsetMm() {
		return offsetMm;
	}

	public void setOffsetMm(Point offsetMm) {
		this.offsetMm = offsetMm;
	}

}
