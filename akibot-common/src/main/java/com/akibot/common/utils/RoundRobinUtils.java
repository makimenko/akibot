package com.akibot.common.utils;

public class RoundRobinUtils {

	private double degrees;

	public RoundRobinUtils(final double degrees) {
		this.degrees = degrees;
	}

	public double add(final double a, final double b) {
		double x = a + b;
		final long rounds = (long) Math.floor(x / degrees);
		if (rounds != 0) {
			x = x - (360 * rounds);
		}
		if (x < 0) {
			x = 360 - x;
		}
		return x;
	}

	public double leftDistance(final double from, final double to) {
		return add(from, -to);
	}

	public double rightDistance(final double from, final double to) {
		return add(to, -from);
	}

	public double modularDistance(final double from, final double to) {
		return Math.min(leftDistance(from, to), rightDistance(from, to));
	}

	public double getDegrees() {
		return degrees;
	}

	public void setDegrees(final double degrees) {
		this.degrees = degrees;
	}

}
