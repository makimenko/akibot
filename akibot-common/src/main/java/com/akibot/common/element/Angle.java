package com.akibot.common.element;

import com.akibot.common.utils.RoundRobinUtils;
import com.akibot.common.utils.VectorUtils;

public class Angle implements SimpleGeometryElement {
	private static final long serialVersionUID = -4825882674847250810L;
	private double radians;

	public Angle() {
		// simple
	}

	public Angle(final double radians) {
		this.radians = radians;
	}

	public double getRadians() {
		return radians;
	}

	public void setRadians(final double radians) {
		this.radians = radians;
	}

	public double getDegrees() {
		return Math.toDegrees(radians);
	}

	public void setDegrees(final double degrees) {
		this.radians = Math.toRadians(degrees);
	}

	public Angle calculateNegativeAngle() {
		return new Angle(-radians);
	}

	public void add(Angle angle) {
		double value = this.radians + angle.getRadians();
		value = normalizeRadian(value);
		this.setRadians(value);
	}

	public double normalizeRadian(double radians) {
		double circle = Math.PI * 2;
		double a = radians % circle;
		if (a < 0) {
			a += circle;
		}
		return a;
	}

	public boolean fuzzyEqual(Angle b, double toleranceDegrees) {
		RoundRobinUtils utils = new RoundRobinUtils(360);
		double res = utils.modularDistance(this.getDegrees(), b.getDegrees());		
		return res < toleranceDegrees;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer(50);
		buf.append("Angle(");
		buf.append(getDegrees());
		buf.append(" deg)");
		return buf.toString();
	}

}
