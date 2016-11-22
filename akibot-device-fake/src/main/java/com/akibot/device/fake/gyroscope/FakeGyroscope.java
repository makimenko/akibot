package com.akibot.device.fake.gyroscope;

import java.util.Random;

import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;

public class FakeGyroscope implements Gyroscope {
	final private Vector3D offset;

	public FakeGyroscope(Vector3D offset) {
		this.offset = offset;
	}

	@Override
	public Vector3D getGyroscopeValue() {
		final Random random = new Random();
		double x = random.nextDouble();
		double y = random.nextDouble();
		double z = random.nextDouble();

		Vector3D vector3D = new Vector3D(x, y, z);
		vector3D.add(offset);

		return vector3D;
	}

}
