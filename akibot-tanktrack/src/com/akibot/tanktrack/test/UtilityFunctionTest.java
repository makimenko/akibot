package com.akibot.tanktrack.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.tanktrack.component.orientation.RoundRobinUtils;

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

}
