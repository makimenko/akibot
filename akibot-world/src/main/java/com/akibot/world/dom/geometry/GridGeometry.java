package com.akibot.world.dom.geometry;

import com.akibot.common.utils.ArrayUtils;
import com.akibot.world.dom.config.GridConfiguration;

public class GridGeometry extends BaseGeometry {
	private static final long serialVersionUID = 1L;
	private GridConfiguration gridConfiguration;
	private int[][] grid;

	public GridGeometry(final GridConfiguration gridConfiguration) {
		this.gridConfiguration = gridConfiguration;
		this.grid = new int[gridConfiguration.getCellCountX()][gridConfiguration.getCellCountY()];

		final ArrayUtils arrayUtils = new ArrayUtils();
		arrayUtils.updateValue(grid, gridConfiguration.getUnknownValue());
	}

	public int getValue(final int x, final int y) {
		return grid[x][y];
	}

	public GridConfiguration getGridConfiguration() {
		return gridConfiguration;
	}

	public void setGridConfiguration(GridConfiguration gridConfiguration) {
		this.gridConfiguration = gridConfiguration;
	}

	public int[][] getGrid() {
		return grid;
	}

	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

}
