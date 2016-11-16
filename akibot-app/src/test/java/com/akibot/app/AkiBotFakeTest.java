package com.akibot.app;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = { "classpath:FakeContext.xml" })
public class AkiBotFakeTest {

	@Autowired
	Gyroscope mainGyroscope;

	@Test
	public void testFakeGyroscope() {
		Vector3D firstValue = mainGyroscope.getGyroscopeValue();
		Vector3D secondValue = mainGyroscope.getGyroscopeValue();
		assertTrue(firstValue != null && secondValue != null);
		assertTrue(!firstValue.equals(secondValue, 0));
		;
	}
}
