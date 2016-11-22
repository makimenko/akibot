package com.akibot.world.dom.geometry;

import com.akibot.world.dao.GridGeometryDao;
import com.akibot.world.dao.GridGeometryDaoImpl;
import com.akibot.world.dom.config.GridConfiguration;

public class GridGeometry extends BaseNodeGeometry {
	private static final long serialVersionUID = 1L;
	private GridConfiguration gridConfiguration;
	private int[][] grid;

	public void setGridConfiguration(final GridConfiguration gridConfiguration) {
		this.gridConfiguration = gridConfiguration;
	}

	public void setGrid(final int[][] grid) {
		this.grid = grid;
	}

	public GridGeometry(final GridConfiguration gridConfiguration) {
		super();
		this.gridConfiguration = gridConfiguration;
		this.grid = new int[gridConfiguration.getCellCountX()][gridConfiguration.getCellCountY()];

		// TODO: Use Spring Autowire?
		final GridGeometryDao gridGeometryDao = new GridGeometryDaoImpl(this);
		gridGeometryDao.reset(gridConfiguration.getUnknownValue());
	}

	public GridConfiguration getGridConfiguration() {
		return gridConfiguration;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getGrid(final int x, final int y) {
		return grid[x][y];
	}
}
