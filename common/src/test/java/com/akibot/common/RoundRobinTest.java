package com.akibot.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.common.utils.RoundRobinUtils;

public class RoundRobinTest {
	private final RoundRobinUtils roundRobinUtils = new RoundRobinUtils(360);

	@Test
	public void testRoundRobinAdd() {
		assertEquals(0, roundRobinUtils.add(0, 0), 0);
		assertEquals(0, roundRobinUtils.add(0, 360), 0);
		assertEquals(0, roundRobinUtils.add(360, 0), 0);
		assertEquals(0, roundRobinUtils.add(360, 360), 0);
		assertEquals(0, roundRobinUtils.add(0, 720), 0);
		assertEquals(2, roundRobinUtils.add(1, 1), 0);
		assertEquals(271, roundRobinUtils.add(270, 1), 0);
		assertEquals(1, roundRobinUtils.add(180, 181), 0);
		assertEquals(1, roundRobinUtils.add(1, 720), 0);
		assertEquals(358, roundRobinUtils.add(-1, -1), 0);
		assertEquals(10, roundRobinUtils.add(350, 20), 0);
		assertEquals(350, roundRobinUtils.add(10, -20), 0);
		assertEquals(0, roundRobinUtils.add(180.25, 179.75), 0);
		assertEquals(180.75, roundRobinUtils.add(180.5, 0.25), 0);
	}

	@Test
	public void testRoundRobinRightDistance() {
		assertEquals(270, roundRobinUtils.rightDistance(180, 90), 0);
		assertEquals(1, roundRobinUtils.rightDistance(180, 181), 0);
		assertEquals(359, roundRobinUtils.rightDistance(181, 180), 0);
		assertEquals(0, roundRobinUtils.rightDistance(180, 180), 0);
		assertEquals(61, roundRobinUtils.rightDistance(299, 0), 0);
		assertEquals(359, roundRobinUtils.rightDistance(330, 329), 0);
	}

	@Test
	public void testRoundRobinLeftDistance() {
		assertEquals(270, roundRobinUtils.leftDistance(180, 270), 0);
		assertEquals(1, roundRobinUtils.leftDistance(180, 179), 0);
		assertEquals(359, roundRobinUtils.leftDistance(179, 180), 0);
		assertEquals(0, roundRobinUtils.leftDistance(180, 180), 0);
		assertEquals(1, roundRobinUtils.leftDistance(330.000000001, 329.000000001), 0);
	}

	@Test
	public void testRoundRobinModularDistance() {
		assertEquals(10, roundRobinUtils.modularDistance(80, 90), 0);
		assertEquals(10, roundRobinUtils.modularDistance(90, 80), 0);
		assertEquals(10, roundRobinUtils.modularDistance(355, 5), 0);
		assertEquals(10, roundRobinUtils.modularDistance(5, 355), 0);
		assertEquals(0, roundRobinUtils.modularDistance(-5, 355), 0);
		assertEquals(0, roundRobinUtils.modularDistance(1, 1), 0);
	}

}
