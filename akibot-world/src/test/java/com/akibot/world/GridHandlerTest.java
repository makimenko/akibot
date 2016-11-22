package com.akibot.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akibot.common.element.Angle;
import com.akibot.common.element.Line2D;
import com.akibot.common.element.Point2D;
import com.akibot.common.element.Vector3D;
import com.akibot.common.utils.CommonUtilsException;
import com.akibot.world.dao.GridGeometryHandler;
import com.akibot.world.dom.config.GridConfiguration;
import com.akibot.world.dom.geometry.GridGeometry;

public class GridHandlerTest {
	private final static double ANGLE_PRECISSION = 0.0000000001;

	@Test
	public void testGridAddPoint() throws CommonUtilsException {
		final int maxObstacle = 2;
		final GridGeometry testGridGeometry = new GridGeometry(new GridConfiguration(6, 5, 10, maxObstacle));
		assertEquals(testGridGeometry.getGridConfiguration().getUnknownValue(), testGridGeometry.getValue(0, 0));

		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGridGeometry);
		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(0, 0));
		gridGeometryHandler.endGridUpdate();
		assertEquals(1, testGridGeometry.getValue(0, 0));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(10, 9));
		gridGeometryHandler.endGridUpdate();
		assertEquals(1, testGridGeometry.getValue(1, 0));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(11, 9));
		gridGeometryHandler.endGridUpdate();
		assertEquals(2, testGridGeometry.getValue(1, 0));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(11, 9));
		gridGeometryHandler.endGridUpdate();
		assertEquals(maxObstacle, testGridGeometry.getValue(1, 0));
	}

	@Test
	public void testGridAddLine() throws CommonUtilsException {
		final GridGeometry testGridGeometry = new GridGeometry(new GridConfiguration(6, 5, 10, 2));
		// ArrayUtils arrayUtils = new ArrayUtils();
		// arrayUtils.printArray(System.out, testGridGeometry.getGrid());
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGridGeometry);

		final int emptyValue = testGridGeometry.getGridConfiguration().getEmptyValue();
		final int unknownValue = testGridGeometry.getGridConfiguration().getUnknownValue();
		assertEquals(unknownValue, testGridGeometry.getValue(5, 4));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2D(new Line2D(new Point2D(0, 0), new Point2D(0, 0)), true);
		gridGeometryHandler.endGridUpdate();
		assertEquals(1, testGridGeometry.getValue(0, 0));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2D(new Line2D(new Point2D(21, 21), new Point2D(29, 49)), true);
		gridGeometryHandler.endGridUpdate();

		assertEquals(emptyValue, testGridGeometry.getValue(2, 2));
		assertEquals(emptyValue, testGridGeometry.getValue(2, 3));
		assertEquals(1, testGridGeometry.getValue(2, 4));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2D(new Line2D(new Point2D(21, 21), new Point2D(29, 49)), true);
		gridGeometryHandler.endGridUpdate();
		// System.out.println("After");
		// arrayUtils.printArray(System.out, testGridGeometry.getGrid());
		assertEquals(emptyValue, testGridGeometry.getValue(2, 2));
		assertEquals(emptyValue, testGridGeometry.getValue(2, 3));
		assertEquals(2, testGridGeometry.getValue(2, 4));

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2D(new Line2D(new Point2D(45, 5), new Point2D(55, 5)), false);
		gridGeometryHandler.endGridUpdate();
		assertEquals(emptyValue, testGridGeometry.getValue(4, 0));
		assertEquals(emptyValue, testGridGeometry.getValue(5, 0));

		// Make sure that other Cells are not affected
		assertEquals(unknownValue, testGridGeometry.getValue(2, 1));
		assertEquals(unknownValue, testGridGeometry.getValue(1, 2));
		assertEquals(unknownValue, testGridGeometry.getValue(1, 3));
		assertEquals(unknownValue, testGridGeometry.getValue(1, 4));
		assertEquals(unknownValue, testGridGeometry.getValue(3, 2));
		assertEquals(unknownValue, testGridGeometry.getValue(3, 3));
		assertEquals(unknownValue, testGridGeometry.getValue(3, 4));

	}

	@Test
	public void testGridRasterize() throws CommonUtilsException {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(4, 4, 1, 1));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);
		final int[][] res = gridGeometryHandler.rasterize2D(new Line2D(new Point2D(3, 3), new Point2D(1, 1)), true);

		assertEquals(3, res[0][0]);
		assertEquals(3, res[0][1]);

		assertEquals(2, res[1][0]);
		assertEquals(2, res[1][1]);

		assertEquals(1, res[2][0]);
		assertEquals(1, res[2][1]);

	}

	@Test
	public void testGridOffset() throws CommonUtilsException {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(2, 2, 10, 1, new Vector3D(-10, -10, 0)));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);

		assertEquals(1, gridGeometryHandler.getAddressX(new Point2D(5, 4)));
		assertEquals(1, gridGeometryHandler.getAddressY(new Point2D(5, 4)));
		assertEquals(0, gridGeometryHandler.getAddressX(new Point2D(-5, -4)));
		assertEquals(0, gridGeometryHandler.getAddressY(new Point2D(-5, -4)));
		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(5, 5));
		gridGeometryHandler.endGridUpdate();
		assertEquals(1, testGrid.getValue(1, 1));
	}

	@Test
	public void testGridOutsideWorld() throws CommonUtilsException {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(2, 2, 10, 1));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);

		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addPoint2D(new Point2D(0, 0));
		gridGeometryHandler.addPoint2D(new Point2D(19, 19));
		gridGeometryHandler.endGridUpdate();
		try {
			gridGeometryHandler.addPoint2D(new Point2D(20, 20));
			assertTrue(false);
		} catch (CommonUtilsException e) {
			assertTrue(true); // expected behavior
		}
		try {
			gridGeometryHandler.addPoint2D(new Point2D(-1, -1));
			assertTrue(false);
		} catch (CommonUtilsException e) {
			assertTrue(true); // expected behavior
		}
	}

	@Test
	public void testGridCalculateLine() {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(1, 1, 1, 1));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);

		final double startPosX0 = 1;
		final double startPosY0 = 2;
		final double distanceCm = 10;
		final Point2D positionOffset = new Point2D(startPosX0, startPosY0);

		Line2D result;

		// Same line
		final Angle angle0 = new Angle();
		angle0.setDegrees(0);
		result = gridGeometryHandler.createLine2D(positionOffset, angle0, distanceCm);
		assertEquals(startPosX0, result.getFrom().getX(), ANGLE_PRECISSION);
		assertEquals(startPosY0, result.getFrom().getY(), ANGLE_PRECISSION);
		assertEquals(startPosX0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(startPosY0 + distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// Opposite direction (180 degrees)
		final Angle angle180 = new Angle();
		angle180.setDegrees(180);
		result = gridGeometryHandler.createLine2D(positionOffset, angle180, distanceCm);
		assertEquals(startPosX0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(startPosY0 - distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// 90 Degrees
		final Angle angle90 = new Angle();
		angle90.setDegrees(90);
		result = gridGeometryHandler.createLine2D(positionOffset, angle90, distanceCm);
		assertEquals(startPosX0 - distanceCm, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(startPosY0, result.getTo().getY(), ANGLE_PRECISSION);

	}

	@Test
	public void testGridAddLineNarrowAngle() throws CommonUtilsException {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(2, 3, 10, 1));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);
		final int emptyValue = testGrid.getGridConfiguration().getEmptyValue();
		final int unknownValue = testGrid.getGridConfiguration().getUnknownValue();

		final Angle angle = new Angle();
		angle.setDegrees(5);
		final Line2D line = new Line2D(new Point2D(5, 15), new Point2D(15, 15));
		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2DWithAngle(line, angle, true);
		gridGeometryHandler.endGridUpdate();

		assertEquals(unknownValue, testGrid.getValue(0, 2));
		assertEquals(unknownValue, testGrid.getValue(1, 2));

		assertEquals(emptyValue, testGrid.getValue(0, 1));
		assertEquals(1, testGrid.getGrid()[1][1]);

		assertEquals(unknownValue, testGrid.getValue(0, 0));
		assertEquals(unknownValue, testGrid.getValue(1, 0));
	}

	@Test
	public void testGridAddLineWideAngle() throws CommonUtilsException {
		final GridGeometry testGrid = new GridGeometry(new GridConfiguration(2, 3, 10, 2));
		final GridGeometryHandler gridGeometryHandler = new GridGeometryHandler(testGrid);
		final int emptyValue = testGrid.getGridConfiguration().getEmptyValue();
		final int unknownValue = testGrid.getGridConfiguration().getUnknownValue();

		final Angle angle = new Angle();
		angle.setDegrees(45);
		final Line2D line2D = new Line2D(new Point2D(5, 15), new Point2D(15, 15));
		gridGeometryHandler.startGridUpdate();
		gridGeometryHandler.addLine2DWithAngle(line2D, angle, true);
		gridGeometryHandler.endGridUpdate();

		assertEquals(unknownValue, testGrid.getValue(0, 2));
		assertEquals(1, testGrid.getValue(1, 2));

		assertEquals(emptyValue, testGrid.getValue(0, 1));
		assertEquals(1, testGrid.getValue(1, 1));

		assertEquals(unknownValue, testGrid.getValue(0, 0));
		assertEquals(1, testGrid.getValue(1, 0));
	}

}
