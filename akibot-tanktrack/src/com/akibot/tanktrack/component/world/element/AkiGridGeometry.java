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

	public int[][] rasterize(AkiLine line, boolean endIsObstacle) {

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

	public void addLineWithAngle(AkiLine line, AkiAngle errorAngle, boolean endIsObstacle) {
		AkiLine lineLeft = rotateLine(line, errorAngle);
		AkiLine lineRight = rotateLine(line, errorAngle.getNegativeAngle());

		addLine(line, endIsObstacle);
		addLine(lineLeft, endIsObstacle);
		addLine(lineRight, endIsObstacle);

		iterateEndOfLine(line, lineLeft, endIsObstacle);
		iterateEndOfLine(line, lineRight, endIsObstacle);
	}

	private void iterateEndOfLine(AkiLine line, AkiLine line2, boolean endIsObstacle) {
		int[][] arrLeft = rasterize(new AkiLine(line2.getTo(), line.getTo()), endIsObstacle);
		if (arrLeft.length > 2) {
			for (int i = 1; i < arrLeft.length - 1; i++) {
				addLine(new AkiLine(line.getFrom(), new AkiPoint(arrLeft[i][0] * akiGridConfiguration.getCellSize(), arrLeft[i][1]
						* akiGridConfiguration.getCellSize(), 0)), endIsObstacle);
			}
		}
	}

	public AkiPoint rotateVector(AkiLine line, AkiAngle angle) {

		double ix0 = line.getFrom().getX();
		double iy0 = line.getFrom().getY();

		double ix1 = line.getTo().getX();
		double iy1 = line.getTo().getY();

		double angleRadians = angle.getRadians();

		double x1 = ix1 - ix0;
		double y1 = iy1 - iy0;

		double x2 = (x1 * Math.cos(angleRadians)) - (y1 * Math.sin(angleRadians)) + ix0;
		double y2 = (y1 * Math.cos(angleRadians)) + (x1 * Math.sin(angleRadians)) + iy0;

		return new AkiPoint(x2, y2, 0); // TODO: Make double everywhere
	}

	public AkiLine rotateLine(AkiLine line, AkiAngle angle) {
		return new AkiLine(line.getFrom(), rotateVector(line, angle));
	}

	public void addDistance(AkiPoint positionOffset, AkiAngle northAngle, AkiAngle errorAngle, double distanceCm, boolean endIsObstacle) {
		AkiLine line = calculateNorthLine(positionOffset, northAngle, distanceCm);
		addLineWithAngle(line, errorAngle, endIsObstacle);
	}

	public AkiLine calculateNorthLine(AkiPoint positionOffset, AkiAngle northAngle, double distanceCm) {
		AkiLine line = new AkiLine();
		line.setFrom(positionOffset);

		double a = distanceCm * Math.cos(northAngle.getRadians() + Math.toRadians(90));
		double b = distanceCm * Math.sin(northAngle.getRadians() + Math.toRadians(90));
		line.setTo(new AkiPoint(positionOffset.getX() + a, positionOffset.getY() + b, 0)); // TODO: calculate
		return line;
	}

	public long getChangeSequence() {
		return changeSequence;
	}

}
