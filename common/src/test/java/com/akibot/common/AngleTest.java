package com.akibot.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akibot.common.element.Angle;
import com.akibot.common.element.Line2D;
import com.akibot.common.element.Point2D;
import com.akibot.common.element.Vector3D;
import com.akibot.common.utils.VectorUtils;

public class AngleTest {
	private final static double ANGLE_PRECISSION = 0.0000000001;
	private double toleranceDeg = 1.0001;
	private Angle angle0 = new Angle(Math.toRadians(0));
	private Angle angle1 = new Angle(Math.toRadians(1));
	private Angle angle2 = new Angle(Math.toRadians(2));
	private Angle angle45 = new Angle(Math.toRadians(45));
	private Angle angle90 = new Angle(Math.toRadians(90));
	private Angle angle90circle = new Angle(Math.toRadians(90 + 360));
	private Angle angle91 = new Angle(Math.toRadians(91));
	private Angle angle92 = new Angle(Math.toRadians(92));
	private Angle angle92negative = new Angle(Math.toRadians(92 - (360 * 10)));
	private Angle angle180 = new Angle(Math.toRadians(180));
	private Angle angle270 = new Angle(Math.toRadians(270));
	private final VectorUtils vectorUtils = new VectorUtils();
	private Vector3D vector;
	private Angle angle;

	@Test
	public void testVectorUtilsRotateLine() {
		final float ix0 = 3;
		final float iy0 = 2;
		final float ix1 = 7;
		final float iy1 = 6;
		final Line2D line = new Line2D();
		line.setFrom(new Point2D(ix0, iy0));
		line.setTo(new Point2D(ix1, iy1));

		Point2D resultPoint2D;
		Line2D resultLine2D;

		// Same line
		final Angle angle0 = new Angle();
		angle0.setDegrees(0);
		resultPoint2D = vectorUtils.rotateEndOfLine2D(line, angle0);
		assertEquals(ix1, resultPoint2D.getX(), ANGLE_PRECISSION);
		assertEquals(iy1, resultPoint2D.getY(), ANGLE_PRECISSION);

		resultLine2D = vectorUtils.rotateLine2D(line, angle0);
		assertEquals(ix0, resultLine2D.getFrom().getX(), ANGLE_PRECISSION);
		assertEquals(iy0, resultLine2D.getFrom().getY(), ANGLE_PRECISSION);
		assertEquals(ix1, resultLine2D.getTo().getX(), ANGLE_PRECISSION);
		assertEquals(iy1, resultLine2D.getTo().getY(), ANGLE_PRECISSION);

		// 90 degrees to the left
		final Angle angleLeft90 = new Angle();
		angleLeft90.setDegrees(90);
		resultPoint2D = vectorUtils.rotateEndOfLine2D(line, angleLeft90);
		assertEquals(iy1, resultPoint2D.getY(), ANGLE_PRECISSION);
		assertTrue(resultPoint2D.getX() < 0);

		// 90 degrees to the right
		final Angle angleRight90 = new Angle();
		angleRight90.setDegrees(-90);
		resultPoint2D = vectorUtils.rotateEndOfLine2D(line, angleRight90);
		assertEquals(ix1, resultPoint2D.getX(), ANGLE_PRECISSION);
		assertTrue(resultPoint2D.getY() < 0);

		resultLine2D = vectorUtils.rotateLine2D(line, angleRight90);
		assertEquals(ix0, resultLine2D.getFrom().getX(), ANGLE_PRECISSION);
		assertEquals(iy0, resultLine2D.getFrom().getY(), ANGLE_PRECISSION);
		assertEquals(ix1, resultLine2D.getTo().getX(), ANGLE_PRECISSION);
		assertTrue(resultLine2D.getTo().getY() < 0);

		// same, 90 degrees to the right (but via negative angle)
		resultPoint2D = vectorUtils.rotateEndOfLine2D(line, angleLeft90.calculateNegativeAngle());
		assertEquals(ix1, resultPoint2D.getX(), ANGLE_PRECISSION);
		assertTrue(resultPoint2D.getY() < 0);
	}

	@Test
	public void testFuzzyEqualRadians() {
		assertTrue(angle90.fuzzyEqual(angle91, toleranceDeg));
		assertTrue(angle90.fuzzyEqual(angle90circle, toleranceDeg));
		assertTrue(!angle90.fuzzyEqual(angle92, toleranceDeg));
		assertTrue(angle92.fuzzyEqual(angle92negative, toleranceDeg));
	}

	@Test
	public void front0() {
		vector = new Vector3D(0, 1, 0);
		angle = vectorUtils.getNorthAngle(vector);
		assertTrue(angle.fuzzyEqual(angle0, toleranceDeg));
		assertTrue(angle.fuzzyEqual(angle1, toleranceDeg));
		assertTrue(!angle.fuzzyEqual(angle2, toleranceDeg));
		assertTrue(!angle.fuzzyEqual(angle90, toleranceDeg));

		vector = new Vector3D(1, 100000000, 0);
		angle = vectorUtils.getNorthAngle(vector);
		assertTrue(angle.fuzzyEqual(angle0, toleranceDeg));
		assertTrue(!angle.fuzzyEqual(angle90, toleranceDeg));
	}

	@Test
	public void left45() {
		vector = new Vector3D(-1, 1, 0);
		angle = vectorUtils.getNorthAngle(vector);
		assertTrue(angle.fuzzyEqual(angle45, toleranceDeg));
	}

	@Test
	public void left90() {
		vector = new Vector3D(-1, 0, 0);
		angle = vectorUtils.getNorthAngle(vector);
		System.out.println(angle);
		assertTrue(angle.fuzzyEqual(angle90, toleranceDeg));
	}

	@Test
	public void back180() {
		vector = new Vector3D(0, -1, 0);
		angle = vectorUtils.getNorthAngle(vector);
		assertTrue(angle.fuzzyEqual(angle180, toleranceDeg));
	}

	@Test
	public void right270() {
		vector = new Vector3D(1, 0, 0);
		angle = vectorUtils.getNorthAngle(vector);
		assertTrue(angle.fuzzyEqual(angle270, toleranceDeg));
	}

}
