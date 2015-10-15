package com.akibot.tanktrack.component.world.element;

import com.akibot.tanktrack.component.world.exception.OutsideWorldException;

public class GridGeometry extends NamedClass implements Geometry {
	private static final long serialVersionUID = 1L;
	private GridConfiguration akiGridConfiguration;
	private int[][] grid;
	private long changeSequence = 0;

	public static final int UNKNOWN_VALUE = -1;
	public static final int EMPTY_VALUE = 0;

	public GridGeometry(GridConfiguration akiGridConfiguration) {
		this.akiGridConfiguration = akiGridConfiguration;
		init();
	}

	private void init() {
		this.grid = new int[akiGridConfiguration.getCellCountX()][akiGridConfiguration.getCellCountY()];
		ArrayUtils.updateValue(grid, UNKNOWN_VALUE);
	}

	public GridConfiguration getAkiGridConfiguration() {
		return akiGridConfiguration;
	}

	public int[][] getGrid() {
		return grid;
	}

	public Point getPointWithOffset(Point point) throws OutsideWorldException {
		Point offsetPoint = new Point(0, 0, 0);
		offsetPoint.setX(point.getX() - akiGridConfiguration.getOffset().getX());
		offsetPoint.setY(point.getY() - akiGridConfiguration.getOffset().getY());
		offsetPoint.setZ(point.getZ() - akiGridConfiguration.getOffset().getZ());

		if (offsetPoint.getX() >= akiGridConfiguration.getCellCountX() * akiGridConfiguration.getCellSize() || offsetPoint.getX() < 0
				|| offsetPoint.getY() >= akiGridConfiguration.getCellCountY() * akiGridConfiguration.getCellSize() || offsetPoint.getY() < 0) {
			throw new OutsideWorldException();
		}
		return offsetPoint;
	}

	public int getAddressX(Point point) throws OutsideWorldException {
		return (int) Math.floor(getPointWithOffset(point).getX() / akiGridConfiguration.getCellSize());
	}

	public int getAddressY(Point point) throws OutsideWorldException {
		return (int) Math.floor(getPointWithOffset(point).getY() / akiGridConfiguration.getCellSize());
	}

	public void addPoint(Point point) throws OutsideWorldException {
		add(getAddressX(point), getAddressY(point));
	}

	public void add(int addressX, int addressY) {
		// System.out.println("add(" + addressX + ", " + addressY + ")");
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

	public void addLine(Line line, boolean endIsObstacle) throws OutsideWorldException {
		int[][] raster = rasterize(line, endIsObstacle);
		for (int i = 0; i < raster.length; i++) {
			int x = raster[i][0];
			int y = raster[i][1];
			int status = raster[i][2];

			if (status == -1) {
				remove(x, y);
			} else if (status == 1) {
				add(x, y);
			}
		}
	}

	public int[][] rasterize(Line line, boolean endIsObstacle) throws OutsideWorldException {

		int x = getAddressX(line.getFrom());
		int y = getAddressY(line.getFrom());

		int x2 = getAddressX(line.getTo());
		int y2 = getAddressY(line.getTo());

		int distance = 1 + (int) Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));

		int[][] res = new int[distance][3];
		int index = 0;

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
			res[index][0] = x;
			res[index][1] = y;
			if (endIsObstacle && x == x2 && y == y2) {
				res[index][2] = 1;
			} else if (!endIsObstacle || x != x2 || y != y2) {
				res[index][2] = -1;
			} else {
				res[index][2] = 0;
			}
			index++;

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
		return res;
	}

	public void addLineWithAngle(Line line, Angle errorAngle, boolean endIsObstacle) throws OutsideWorldException {
		Line lineLeft = VectorUtils.rotateLine(line, errorAngle);
		Line lineRight = VectorUtils.rotateLine(line, errorAngle.getNegativeAngle());

		addLine(line, endIsObstacle);
		addLine(lineLeft, endIsObstacle);
		addLine(lineRight, endIsObstacle);

		iterateEndOfLine(line, lineLeft, endIsObstacle);
		iterateEndOfLine(line, lineRight, endIsObstacle);
	}

	private void iterateEndOfLine(Line line, Line line2, boolean endIsObstacle) throws OutsideWorldException {
		int[][] arrLeft = rasterize(new Line(line2.getTo(), line.getTo()), endIsObstacle);
		if (arrLeft.length > 2) {
			for (int i = 1; i < arrLeft.length - 1; i++) {
				addLine(new Line(line.getFrom(), new Point(arrLeft[i][0] * akiGridConfiguration.getCellSize(), arrLeft[i][1]
						* akiGridConfiguration.getCellSize(), 0)), endIsObstacle);
			}
		}
	}

	public void addDistance(DistanceDetails distanceDetails) throws OutsideWorldException {
		Line line = calculateNorthLine(distanceDetails.getPositionOffset(), distanceDetails.getNorthAngle(), distanceDetails.getDistanceCm());
		addLineWithAngle(line, distanceDetails.getErrorAngle(), distanceDetails.isEndObstacle());
	}

	public Line calculateNorthLine(Point positionOffset, Angle northAngle, double distanceCm) {
		Line line = new Line();
		line.setFrom(positionOffset);

		double a = distanceCm * Math.cos(northAngle.getRadians() + Math.toRadians(90));
		double b = distanceCm * Math.sin(northAngle.getRadians() + Math.toRadians(90));
		line.setTo(new Point(positionOffset.getX() + a, positionOffset.getY() + b, 0)); // TODO: calculate
		return line;
	}

	public long getChangeSequence() {
		return changeSequence;
	}

}
