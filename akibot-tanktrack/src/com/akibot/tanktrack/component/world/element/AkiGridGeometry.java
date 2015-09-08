package com.akibot.tanktrack.component.world.element;

public class AkiGridGeometry extends AkiNamedClass implements AkiGeometry {
	private static final long serialVersionUID = 1L;
	private AkiGridConfiguration akiGridConfiguration;
	private int[][] grid;
	private long changeSequence = 0;

	public static final int UNKNOWN_VALUE = -1;
	public static final int EMPTY_VALUE = 0;

	public AkiGridGeometry(AkiGridConfiguration akiGridConfiguration) {
		this.akiGridConfiguration = akiGridConfiguration;
		init();
	}

	private void init() {
		this.grid = new int[akiGridConfiguration.getCellCountX()][akiGridConfiguration.getCellCountY()];
		ArrayUtils.updateValue(grid, UNKNOWN_VALUE);
	}

	public AkiGridConfiguration getAkiGridConfiguration() {
		return akiGridConfiguration;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getAddressX(AkiPoint point) {
		return (int) Math.floor(point.getX() / akiGridConfiguration.getCellSize());
	}

	public int getAddressY(AkiPoint point) {
		return (int) Math.floor(point.getY() / akiGridConfiguration.getCellSize());
	}

	public void addPoint(AkiPoint point) {
		add(getAddressX(point), getAddressY(point));
	}

	public void add(int addressX, int addressY) {
		// System.out.println("add(" + x + ", " + y + ")");
		if (grid[addressX][addressY] == UNKNOWN_VALUE) {
			grid[addressX][addressY] = 1;
			changeSequence++;
		} else if (grid[addressX][addressY] < akiGridConfiguration.getMaxObstacleCount()) {
			grid[addressX][addressY]++;
			changeSequence++;
		}
	}

	public void remove(int addressX, int addressY) {
		// System.out.println("remove(" + x + ", " + y + ")");
		int v = grid[addressX][addressY];

		if (v == UNKNOWN_VALUE) {
			grid[addressX][addressY] = EMPTY_VALUE;
			changeSequence++;
		} else if (v > EMPTY_VALUE) {
			grid[addressX][addressY]--;
			changeSequence++;
		}
	}

	public void addLine(AkiLine line, boolean endIsObstacle) {
		int x = getAddressX(line.getFrom());
		int y = getAddressY(line.getFrom());

		int x2 = getAddressX(line.getTo());
		int y2 = getAddressY(line.getTo());

		// Bresenham's line algorithm
		int w = x2 - x;
		int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			if (!endIsObstacle || x != x2 || y != y2) {
				remove(x, y);
			}
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
		if (endIsObstacle) {
			add(x2, y2);
		}
	}

	public void addLineWithAngle(AkiLine line, AkiAngle errorAngle, boolean endIsObstacle) {
		// TODO: implement angle:
		// 1. get end line, rasterize it
		// 2. loop across end raster
		// 3. add / remove obstacle once?
		addLine(line, endIsObstacle);
	}

	public void addDistance(AkiPoint positionOffset, AkiAngle northAngle, AkiAngle errorAngle, float distanceCm, boolean endIsObstacle) {
		AkiLine line = calculateLine(positionOffset, northAngle, distanceCm);
		addLineWithAngle(line, errorAngle, endIsObstacle);
	}

	public AkiLine calculateLine(AkiPoint positionOffset, AkiAngle northAngle, float distanceCm) {
		// TODO: Implement
		AkiLine line = new AkiLine();
		line.setFrom(new AkiPoint(0, 0, 0)); // replace
		line.setTo(new AkiPoint(0, 0, 0)); // replace
		return line;
	}

	public long getChangeSequence() {
		return changeSequence;
	}

}
