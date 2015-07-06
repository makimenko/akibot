package com.akibot.tanktrack.component.orientation;

public class RoundRobinUtils {

	private double degrees;

	public RoundRobinUtils(double degrees) {
		this.degrees = degrees;

	}

	public double add(double a, double b) {
		double x = a + b;
		long rounds = (long) Math.floor(x / degrees);
		if (rounds != 0) {
			x = x - (360 * rounds);
		}
		if (x < 0) {
			x = 360 - x;
		}
		return x;
	}

	public double leftDistance(double from, double to) {
		double x = add(from, -to);
		return x;
	}

	public double rightDistance(double from, double to) {
		double x = add(to, -from);
		return x;
	}

}
