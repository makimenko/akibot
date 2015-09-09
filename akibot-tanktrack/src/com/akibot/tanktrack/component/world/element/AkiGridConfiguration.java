package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiGridConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;
	private int cellCountX;
	private int cellCountY;
	private double cellSize;
	private int maxObstacleCount;

	public AkiGridConfiguration() {

	}

	public AkiGridConfiguration(int cellCountX, int cellCountY, float cellSize, int maxObstacleCount) {
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
		this.cellSize = cellSize;
		this.maxObstacleCount = maxObstacleCount;
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

	public double getCellSize() {
		return cellSize;
	}

	public void setCellSize(double cellSize) {
		this.cellSize = cellSize;
	}

	public int getMaxObstacleCount() {
		return maxObstacleCount;
	}

	public void setMaxObstacleCount(int maxObstacleCount) {
		this.maxObstacleCount = maxObstacleCount;
	}

}
