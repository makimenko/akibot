package com.akibot.device.fake.distance;

import java.util.Random;

import com.akibot.common.device.CallableDistanceSensor;
import com.akibot.common.element.Angle;
import com.akibot.common.element.Distance;

public class FakeCallableDistanceSensor implements CallableDistanceSensor {
	private final Random random;
	private final double multiplier;
	private final double offset;
	private final boolean endObstacle;
	private final Angle errorAngle;

	public FakeCallableDistanceSensor(double multiplier, double offset, boolean endObstacle, Angle errorAngle) {
		this.random = new Random();
		this.offset = offset;
		this.multiplier = multiplier;
		this.endObstacle = endObstacle;
		this.errorAngle = errorAngle;
	}

	@Override
	public Distance getDistance() {
		double distanceMm = random.nextDouble();
		distanceMm *= multiplier;
		distanceMm += offset;

		Distance distance = new Distance();
		distance.setEndObstacle(this.endObstacle);
		distance.setErrorAngle(this.errorAngle);
		distance.setDistanceMm(distanceMm);

		return distance;
	}

}
