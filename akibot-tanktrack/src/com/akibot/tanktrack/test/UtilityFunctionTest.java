package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.tanktrack.component.orientation.RoundRobinUtils;
import com.akibot.tanktrack.component.world.element.AkiAngle;
import com.akibot.tanktrack.component.world.element.AkiGrid;
import com.akibot.tanktrack.component.world.element.AkiGridConfiguration;
import com.akibot.tanktrack.component.world.element.AkiLine;
import com.akibot.tanktrack.component.world.element.AkiPoint;
import com.akibot.tanktrack.component.world.element.ArrayUtils;

public class UtilityFunctionTest {

	private RoundRobinUtils robinUtils = new RoundRobinUtils(360);

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
		AkiGrid akiGrid = new AkiGrid(new AkiGridConfiguration(6, 5, 10, maxObstacle));
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);

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
		AkiGrid akiGrid = new AkiGrid(new AkiGridConfiguration(6, 5, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[5][4]);
		assertEquals(0, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(0, 0, 0), new AkiPoint(0, 0, 0)), true);
		assertEquals(1, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(21, 21, 0), new AkiPoint(29, 49, 0)), true);

		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(1, akiGrid.getGrid()[2][4]);
		assertEquals(4, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(21, 21, 0), new AkiPoint(29, 49, 0)), true);

		// System.out.println("After");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[2][2]);
		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[2][3]);
		assertEquals(2, akiGrid.getGrid()[2][4]);
		assertEquals(5, akiGrid.getChangeSequence());

		akiGrid.addLine(new AkiLine(new AkiPoint(45, 5, 0), new AkiPoint(55, 5, 0)), false);

		// System.out.println("After 2");
		// ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[4][0]);
		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[5][0]);
		assertEquals(7, akiGrid.getChangeSequence());

		// Make sure that other Cells are not affected
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[2][1]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][3]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][4]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[3][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[3][3]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[3][4]);

	}

	@Test
	public void gridAddLineNarrowAngle() {
		AkiGrid akiGrid = new AkiGrid(new AkiGridConfiguration(2, 3, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		AkiAngle angle = new AkiAngle();
		angle.setDegrees(5);
		AkiLine line = new AkiLine(new AkiPoint(5, 15, 0), new AkiPoint(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);

		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][0]);
		assertEquals(2, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddLineWideAngle() {
		// TODO: Implement

		AkiGrid akiGrid = new AkiGrid(new AkiGridConfiguration(2, 3, 10, 2));
		// System.out.println("Before");
		// ArrayUtils.printArray(akiGrid.getGrid());

		AkiAngle angle = new AkiAngle();
		angle.setDegrees(45);
		AkiLine line = new AkiLine(new AkiPoint(5, 15, 0), new AkiPoint(15, 15, 0));
		akiGrid.addLineWithAngle(line, angle, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(1, akiGrid.getGrid()[1][2]);

		assertEquals(AkiGrid.EMPTY_VALUE, akiGrid.getGrid()[0][1]);
		assertEquals(1, akiGrid.getGrid()[1][1]);

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][0]);
		assertEquals(1, akiGrid.getGrid()[1][0]);
		assertEquals(4, akiGrid.getChangeSequence());
	}

	@Test
	public void gridAddDistance() {
		// TODO: Implement

		AkiGrid akiGrid = new AkiGrid(new AkiGridConfiguration(3, 3, 10, 2));

		AkiPoint positionOffset = new AkiPoint(15, 15, 0);
		AkiAngle northAngle = new AkiAngle();
		northAngle.setDegrees(90);

		AkiAngle errorAngle = new AkiAngle();
		errorAngle.setDegrees(1);

		akiGrid.addDistance(positionOffset, northAngle, errorAngle, 10, true);
		ArrayUtils.printArray(akiGrid.getGrid());

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(1, akiGrid.getGrid()[1][2]);
		assertEquals(1, akiGrid.getGrid()[2][2]);

		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[0][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[1][2]);
		assertEquals(AkiGrid.UNKNOWN_VALUE, akiGrid.getGrid()[2][2]);

		assertEquals(2, akiGrid.getChangeSequence());
	}

}
