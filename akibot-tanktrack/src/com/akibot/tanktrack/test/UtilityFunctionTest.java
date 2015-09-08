package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.world.element.AkiAngle;
import com.akibot.tanktrack.component.world.element.AkiGridConfiguration;
import com.akibot.tanktrack.component.world.element.AkiGridGeometry;
import com.akibot.tanktrack.component.world.element.AkiLine;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.element.ArrayUtils;

public class UtilityFunctionTest {

	private RoundRobinUtils robinUtils = new RoundRobinUtils(360);
	private final double ANGLE_PRECISSION = 0.0000000001;

	@Test
	public void roundRobinAdd() {
		assertEquals(0, robinUtils.add(0, 0), 0);
		assertEquals(0, robinUtils.add(0, 360), 0);
		assertEquals(0, robinUtils.add(360, 0), 0);
		assertEquals(0, robinUtils.add(360, 360), 0);
		assertEquals(0, robinUtils.add(0, 720), 0);
		assertEquals(2, robinUtils.add(1, 1), 0);
		assertEquals(271, robinUtils.add(270, 1), 0);
		assertEquals(1, robinUtils.add(180, 181), 0);
		assertEquals(1, robinUtils.add(1, 720), 0);
		assertEquals(358, robinUtils.add(-1, -1), 0);
		assertEquals(10, robinUtils.add(350, 20), 0);
		assertEquals(350, robinUtils.add(10, -20), 0);
		assertEquals(0, robinUtils.add(180.25, 179.75), 0);
		assertEquals(180.75, robinUtils.add(180.5, 0.25), 0);
	}

	@Test
	public void roundRobinRightDistance() {
		assertEquals(270, robinUtils.rightDistance(180, 90), 0);
		assertEquals(1, robinUtils.rightDistance(180, 181), 0);
		assertEquals(359, robinUtils.rightDistance(181, 180), 0);
		assertEquals(0, robinUtils.rightDistance(180, 180), 0);
		assertEquals(61, robinUtils.rightDistance(299, 0), 0);

		assertEquals(359, robinUtils.rightDistance(330, 329), 0);

	}

	@Test
	public void roundRobinLeftDistance() {
		assertEquals(270, robinUtils.leftDistance(180, 270), 0);
		assertEquals(1, robinUtils.leftDistance(180, 179), 0);
		assertEquals(359, robinUtils.leftDistance(179, 180), 0);
		assertEquals(0, robinUtils.leftDistance(180, 180), 0);
		assertEquals(1, robinUtils.leftDistance(330.000000001, 329.000000001), 0);
	}

	@Test
	public void roundRobinModularDistance() {
		assertEquals(10, robinUtils.modularDistance(80, 90), 0);
		assertEquals(10, robinUtils.modularDistance(90, 80), 0);
		assertEquals(10, robinUtils.modularDistance(355, 5), 0);
		assertEquals(10, robinUtils.modularDistance(5, 355), 0);
		assertEquals(0, robinUtils.modularDistance(-5, 355), 0);
		assertEquals(0, robinUtils.modularDistance(1, 1), 0);
	}

	@Test
	public void gridAddPoint() {
		int maxObstacle = 2;
		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(6, 5, 10, maxObstacle));
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);

		akiGrid.addPoint(new AkiPoint(0, 0, 0));
		assertEquals(1, akiGrid.getGrid()[0][0]);

		akiGrid.addPoint(new AkiPoint(10, 9, 0));
		assertEquals(1, akiGrid.getGrid()[1][0]);
		akiGrid.addPoint(new AkiPoint(11, 9, 0));
		assertEquals(2, akiGrid.getGrid()[1][0]);

		assertEquals(3, akiGrid.getChangeSequence());

		akiGrid.addPoint(new AkiPoint(11, 9, 0));
		assertEquals(maxObstacle, akiGrid.getGrid()[1][0]);
		assertEquals(3, akiGrid.getChangeSequence());

	}

	@Test
	public void gridAddLine() {
		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(6, 5, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[5][4]);
		assertEquals(0, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(0, 0, 0), new AkiPoint(0, 0, 0)), true);
		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(21, 21, 0), new AkiPoint(29, 49, 0)), true);

		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(1, akiGrid.getGrid()[2][4]);
		assertEquals(4, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(21, 21, 0), new AkiPoint(29, 49, 0)), true);

		// System.out.println("After");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(2, akiGrid.getGrid()[2][4]);
		assertEquals(5, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(45, 5, 0), new AkiPoint(55, 5, 0)), false);

		// System.out.println("After 2");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[4][0]);
		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[5][0]);
		assertEquals(7, akiGrid.getChangeSequence());

		// Make sure that other Cells are not affected
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][1]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][3]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][4]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][3]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[3][4]);

	}

	@Test
	public void gridAddLineNarrowAngle() {
		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(2, 3, 10, 1));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		AkiAngle angle = new AkiAngle();
		angle.setDegrees(5);
		AkiLine line = new AkiLine(new AkiPoint(5, 15, 0), new AkiPoint(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);

		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][0]);
		assertEquals(2, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddLineWideAngle() {
		// TODO: Implement

		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(2, 3, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		AkiAngle angle = new AkiAngle();
		angle.setDegrees(45);
		AkiLine line = new AkiLine(new AkiPoint(5, 15, 0), new AkiPoint(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(1, akiGrid.getGrid()[1][2]);

		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]);
		assertEquals(4, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddDistance0Angle() {
		// TODO: Implement

		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(3, 3, 10, 1));

		AkiPoint positionOffset = new AkiPoint(15, 15, 0);
		AkiAngle northAngle = new AkiAngle();
		northAngle.setDegrees(-90);

		AkiAngle errorAngle = new AkiAngle();
		errorAngle.setDegrees(1);

		akiGrid.addDistance(positionOffset, northAngle, errorAngle, 10, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[1][1]);
		assertEquals(1, akiGrid.getGrid()[2][1]);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][0]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][0]);

		assertEquals(2, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddDistance45Angle() {
		// TODO: Implement

		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(3, 3, 10, 1));

		AkiPoint positionOffset = new AkiPoint(15, 15, 0);
		AkiAngle northAngle = new AkiAngle();
		northAngle.setDegrees(180);

		AkiAngle errorAngle = new AkiAngle();
		errorAngle.setDegrees(45);

		akiGrid.addDistance(positionOffset, northAngle, errorAngle, 10, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(AkiGridGeometry.EMPTY_VALUE, akiGrid.getGrid()[1][1]);
		assertEquals(AkiGridGeometry.UNKNOWN_VALUE, akiGrid.getGrid()[2][1]);

		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]);
		assertEquals(1, akiGrid.getGrid()[2][0]);

		assertEquals(4, akiGrid.getChangeSequence());
	}

	@Test
	public void gridRotateVector() {
		float ix0 = 3;
		float iy0 = 2;

		float ix1 = 7;
		float iy1 = 6;

		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(1, 1, 1, 1));
		AkiLine line = new AkiLine();
		line.setFrom(new AkiPoint(ix0, iy0, 0));
		line.setTo(new AkiPoint(ix1, iy1, 0));

		AkiPoint result;

		// Same line
		AkiAngle angle0 = new AkiAngle();
		angle0.setDegrees(0);
		result = akiGrid.rotateVector(line, angle0);
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(iy1, result.getY(), ANGLE_PRECISSION);

		// 90 degrees to the left
		AkiAngle angleLeft90 = new AkiAngle();
		angleLeft90.setDegrees(90);
		result = akiGrid.rotateVector(line, angleLeft90);
		assertEquals(iy1, result.getY(), ANGLE_PRECISSION);
		assertEquals(true, result.getX() < 0);

		// 90 degrees to the right
		AkiAngle angleRight90 = new AkiAngle();
		angleRight90.setDegrees(-90);
		result = akiGrid.rotateVector(line, angleRight90);
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(true, result.getY() < 0);

		// same, 90 degrees to the right (but via negative angle)
		result = akiGrid.rotateVector(line, angleLeft90.getNegativeAngle());
		assertEquals(ix1, result.getX(), ANGLE_PRECISSION);
		assertEquals(true, result.getY() < 0);

	}

	@Test
	public void gridCalculateLine() {

		AkiGridGeometry akiGrid = new AkiGridGeometry(new AkiGridConfiguration(1, 1, 1, 1));
		double x0 = 1;
		double y0 = 2;
		double distanceCm = 10;
		AkiPoint positionOffset = new AkiPoint(x0, y0, 0);

		AkiLine result;

		// Same line
		AkiAngle angle0 = new AkiAngle();
		angle0.setDegrees(0);
		result = akiGrid.calculateNorthLine(positionOffset, angle0, distanceCm);
		assertEquals(x0, result.getFrom().getX(), ANGLE_PRECISSION);
		assertEquals(y0, result.getFrom().getY(), ANGLE_PRECISSION);
		assertEquals(x0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0 + distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// Opposite direction (180 degrees)
		AkiAngle angle180 = new AkiAngle();
		angle180.setDegrees(180);
		result = akiGrid.calculateNorthLine(positionOffset, angle180, distanceCm);
		assertEquals(x0, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0 - distanceCm, result.getTo().getY(), ANGLE_PRECISSION);

		// 90 Degrees
		AkiAngle angle90 = new AkiAngle();
		angle90.setDegrees(90);
		result = akiGrid.calculateNorthLine(positionOffset, angle90, distanceCm);
		assertEquals(x0 - distanceCm, result.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(y0, result.getTo().getY(), ANGLE_PRECISSION);

	}

}
