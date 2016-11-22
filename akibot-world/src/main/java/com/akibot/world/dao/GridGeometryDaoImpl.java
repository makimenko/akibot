package com.akibot.world.dao;

import java.util.HashMap;
import java.util.Map;

import com.akibot.common.element.Angle;
import com.akibot.common.element.Distance;
import com.akibot.common.element.Line2D;
import com.akibot.common.element.Point2D;
import com.akibot.common.element.Vector2D;
import com.akibot.common.utils.ArrayUtils;
import com.akibot.common.utils.CommonUtilsException;
import com.akibot.common.utils.VectorUtils;
import com.akibot.world.dom.config.GridConfiguration;
import com.akibot.world.dom.geometry.GridGeometry;
import com.akibot.world.dom.geometry.GridGeometryCellValue;
import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;
import com.akibot.world.message.GridGeometryPartialTransformationEvent;

//TODO: Create new GridGeometry using this DAO?
public class GridGeometryDaoImpl implements GridGeometryDao {
	private static final int RASTER_OBSTACLE = 1;
	private static final int RASTER_EMPTY = -1;

	final private GridGeometry gridGeometry;
	final private GridConfiguration gridConfiguration;
	final private VectorUtils vectorUtils;
	private Map<String, GridGeometryCellValue> gridUpdates;

	private GridGeometryPartialTransformationEvent changeEvent;

	public GridGeometryDaoImpl(final GridGeometry gridGeometry) {
		this.gridGeometry = gridGeometry;
		this.gridConfiguration = gridGeometry.getGridConfiguration();
		this.vectorUtils = new VectorUtils();
	}

	@Override
	public void reset(int value) {
		final ArrayUtils arrayUtils = new ArrayUtils();
		arrayUtils.updateValue(gridGeometry.getGrid(), value);
	}

	public void reset() {
		reset(gridConfiguration.getUnknownValue());
	}

	public Point2D getPoint2DWithOffset(final Point2D point2D) throws CommonUtilsException {
		final Point2D offsetPoint = new Point2D(0, 0);
		offsetPoint.setX(point2D.getX() - gridConfiguration.getOffsetVector().getX());
		offsetPoint.setY(point2D.getY() - gridConfiguration.getOffsetVector().getY());

		if (offsetPoint.getX() >= gridConfiguration.getCellCountX() * gridConfiguration.getCellSizeMm()
				|| offsetPoint.getX() < 0
				|| offsetPoint.getY() >= gridConfiguration.getCellCountY() * gridConfiguration.getCellSizeMm()
				|| offsetPoint.getY() < 0) {
			throw new CommonUtilsException("Outside World: point=" + point2D + ", offsetPoint=" + offsetPoint);
		}
		return offsetPoint;
	}

	@Override
	public int getAddressX(final Point2D point2D) throws CommonUtilsException {
		return (int) Math.floor(getPoint2DWithOffset(point2D).getX() / gridConfiguration.getCellSizeMm());
	}

	@Override
	public int getAddressY(final Point2D point) throws CommonUtilsException {
		return (int) Math.floor(getPoint2DWithOffset(point).getY() / gridConfiguration.getCellSizeMm());
	}

	@Override
	public void addPoint2D(final Point2D point2D) throws CommonUtilsException {
		add(getAddressX(point2D), getAddressY(point2D));
	}

	public void add(final int addressX, final int addressY) {
		int[][] grid = gridGeometry.getGrid();
		if (grid[addressX][addressY] == gridConfiguration.getUnknownValue()) {
			updateGridCellvalue(grid, new GridGeometryCellValue(addressX, addressY, 1));
		} else if (grid[addressX][addressY] < gridConfiguration.getMaxObstacleCount()) {
			updateGridCellvalue(grid, new GridGeometryCellValue(addressX, addressY, grid[addressX][addressY] + 1));
		}
	}

	public void remove(final int addressX, final int addressY) {
		int[][] grid = gridGeometry.getGrid();
		final int v = grid[addressX][addressY];
		if (v == gridConfiguration.getUnknownValue()) {
			updateGridCellvalue(grid, new GridGeometryCellValue(addressX, addressY, gridConfiguration.getEmptyValue()));
		} else if (v > gridConfiguration.getEmptyValue()) {
			updateGridCellvalue(grid, new GridGeometryCellValue(addressX, addressY, grid[addressX][addressY] - 1));
		}
	}

	public void updateGridCellvalue(int[][] grid, GridGeometryCellValue gridCell) {
		gridUpdates.put(gridCell.getAddress(), gridCell);
	}

	@Override
	public void startGridUpdate() {
		this.gridUpdates = new HashMap<String, GridGeometryCellValue>();
	}

	@Override
	public void endGridUpdate() {
		int[][] grid = gridGeometry.getGrid();
		for (GridGeometryCellValue cell : gridUpdates.values()) {
			grid[cell.getX()][cell.getY()] = cell.getValue();
			if (this.changeEvent != null) {
				this.changeEvent.getChangeLog().add(cell);
			}
		}
	}

	@Override
	public void addLine2D(final Line2D line2D, final boolean endIsObstacle) throws CommonUtilsException {
		final int[][] raster = rasterize2D(line2D, endIsObstacle);
		for (int i = 0; i < raster.length; i++) {
			final int x = raster[i][0];
			final int y = raster[i][1];
			final int status = raster[i][2];

			if (status == RASTER_EMPTY) {
				remove(x, y);
			} else if (status == RASTER_OBSTACLE) {
				add(x, y);
			}
		}
	}

	/**
	 * Bresenham's line algorithm. Converts {@link Line2D} to raster (X Y Value)
	 * 
	 * @param line
	 *            {@link Line2D} to be rasterized
	 * @param endIsObstacle
	 *            Set {@link #RASTER_OBSTACLE} at the end of line
	 * @return 2-dimension array: <br/>
	 *         1) Index (flexible/dynamic value, depends on line size); <br/>
	 *         2) Fixed-length with 3 values: 0=x, 1=y, 2=value (
	 *         {@link #RASTER_OBSTACLE} or {@link #RASTER_EMPTY})
	 * @throws CommonUtilsException
	 *             any logical exception (eg out of range)
	 */
	@Override
	public int[][] rasterize2D(final Line2D line, final boolean endIsObstacle) throws CommonUtilsException {

		int x = getAddressX(line.getFrom());
		int y = getAddressY(line.getFrom());

		final int x2 = getAddressX(line.getTo());
		final int y2 = getAddressY(line.getTo());

		final int distance = 1 + (int) Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));

		int[][] res = new int[distance][3];
		int index = 0;

		// Bresenham's line algorithm
		final int w = x2 - x;
		final int h = y2 - y;
		int dx1 = 0;
		int dy1 = 0;
		int dx2 = 0;
		int dy2 = 0;

		if (w < 0) {
			dx1 = -1;
		} else if (w > 0) {
			dx1 = 1;
		}

		if (h < 0) {
			dy1 = -1;
		} else if (h > 0) {
			dy1 = 1;
		}

		if (w < 0) {
			dx2 = -1;
		} else if (w > 0) {
			dx2 = 1;
		}

		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (longest <= shortest) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0) {
				dy2 = -1;
			} else if (h > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}

		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			res[index][0] = x;
			res[index][1] = y;
			if (endIsObstacle && x == x2 && y == y2) {
				res[index][2] = RASTER_OBSTACLE;
			} else {
				res[index][2] = RASTER_EMPTY;
			}
			index++;

			numerator += shortest;
			if (numerator < longest) {
				x += dx2;
				y += dy2;
			} else {
				numerator -= longest;
				x += dx1;
				y += dy1;
			}
		}
		return res;
	}

	@Override
	public Line2D createLine2D(final Point2D startPoint2D, final Angle northAngle, final double distanceCm) {
		final Line2D line2D = new Line2D();
		line2D.setFrom(startPoint2D);
		final double a = distanceCm * Math.cos(northAngle.getRadians() + Math.toRadians(90));
		final double b = distanceCm * Math.sin(northAngle.getRadians() + Math.toRadians(90));
		line2D.setTo(new Point2D(startPoint2D.getX() + a, startPoint2D.getY() + b));
		return line2D;
	}

	@Override
	public void addLine2DWithAngle(final Line2D line2D, final Angle errorAngle, final boolean endIsObstacle)
			throws CommonUtilsException {
		startGridUpdate();
		final VectorUtils vectorUtils = new VectorUtils();
		final Line2D lineLeft = vectorUtils.rotateLine2D(line2D, errorAngle);
		final Line2D lineRight = vectorUtils.rotateLine2D(line2D, errorAngle.calculateNegativeAngle());

		if (errorAngle.fuzzyEqual(new Angle(0), 0.01)) {
			addLine2D(line2D, endIsObstacle);
		} else {
			addLine2D(line2D, endIsObstacle);
			addLine2D(lineLeft, endIsObstacle);
			addLine2D(lineRight, endIsObstacle);

			iterateEndOfLine(line2D, lineLeft, endIsObstacle);
			iterateEndOfLine(line2D, lineRight, endIsObstacle);
		}
		endGridUpdate();
	}

	private void iterateEndOfLine(final Line2D line2Da, final Line2D line2Db, final boolean endIsObstacle)
			throws CommonUtilsException {
		final int[][] arrLeft = rasterize2D(new Line2D(line2Db.getTo(), line2Da.getTo()), endIsObstacle);
		if (arrLeft.length > 2) {
			for (int i = 1; i < arrLeft.length - 1; i++) {
				final Line2D line2D = new Line2D(line2Da.getFrom(),
						new Point2D(
								arrLeft[i][0] * gridConfiguration.getCellSizeMm()
										+ gridConfiguration.getOffsetVector().getX(),
								arrLeft[i][1] * gridConfiguration.getCellSizeMm()
										+ gridConfiguration.getOffsetVector().getY()));
				addLine2D(line2D, endIsObstacle);
			}
		}
	}

	public Line2D calculateNorthLine(final Point2D positionOffset, final Angle northAngle, final double distanceMm) {
		final Line2D line = new Line2D();
		line.setFrom(positionOffset);
		final double a = distanceMm * Math.cos(northAngle.getRadians() + Math.toRadians(90));
		final double b = distanceMm * Math.sin(northAngle.getRadians() + Math.toRadians(90));
		line.setTo(new Point2D(positionOffset.getX() + a, positionOffset.getY() + b));
		return line;
	}

	public void addDistance(Distance distance, NodeTransformation3D relativeTransformation)
			throws CommonUtilsException {
		// TODO: change Point2D to Point3D
		Point2D positionOffset = new Point2D(relativeTransformation.getPosition().getX(),
				relativeTransformation.getPosition().getY());
		Angle northAngle = new Angle(relativeTransformation.getRotation().getZ());
		Line2D line2D = calculateNorthLine(positionOffset, northAngle, distance.getDistanceMm());
		addLine2DWithAngle(line2D, distance.getErrorAngle(), distance.isEndObstacle());
	}

	public NodeTransformation3D calculateRelativeTransformation(Node nodeA, Node nodeB) throws CommonUtilsException {
		if (nodeA == null || nodeB == null) {
			final StringBuffer buf = new StringBuffer(200);
			buf.append("Calculate Relative Transformation failed, because nodes are undefined (nodeA=").append(nodeA)
					.append(", nodeB=").append(nodeB).append(")!");
			throw new CommonUtilsException(buf.toString());
		}

		NodeTransformation3D cumulativeTransformation;

		if (nodeB.equals(nodeA)) {
			cumulativeTransformation = new NodeTransformation3D();
			cumulativeTransformation.resetToDefaults();
		} else {
			cumulativeTransformation = calculateRelativeTransformation(nodeA, nodeB.getParentNode());
			if (nodeB.getTransformation() != null) {
				// TODO: change Vector2D to Vector3D
				Vector2D posVector = new Vector2D(nodeB.getTransformation().getPosition().getX(),
						nodeB.getTransformation().getPosition().getY());
				if (cumulativeTransformation.getRotation().getZ() != 0) {
					Angle angle = new Angle(cumulativeTransformation.getRotation().getZ());

					posVector = vectorUtils.rotate2DVector(posVector, angle);
				}
				cumulativeTransformation.getPosition().add(posVector);
				cumulativeTransformation.getRotation().add(nodeB.getTransformation().getRotation());
			}
		}

		return cumulativeTransformation;
	}

	public void updateGridDistance(Node gridNode, Node distanceNode, Distance distance) throws CommonUtilsException {
		NodeTransformation3D relativeTransformation = calculateRelativeTransformation(gridNode, distanceNode);

		// TODO: TODO: is Ok to remove?
		// double angle = relativeTransformation.getRotation().getZ();
		// angle += distance.getNorthAngle().getRadians();
		// Angle relativeAngle = new Angle(angle);

		addDistance(distance, relativeTransformation);
	}

	public GridGeometryPartialTransformationEvent getChangeEvent() {
		return changeEvent;
	}

	public void setChangeEvent(GridGeometryPartialTransformationEvent changeEvent) {
		this.changeEvent = changeEvent;
	}

}
