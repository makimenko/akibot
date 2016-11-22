package com.akibot.app;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.akibot.app.logic.Workflow;
import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("fake")
@ContextConfiguration(locations = { "classpath:ApplicationContext.xml" })
public class AkiBotFakeTest {

	@Autowired
	Gyroscope mainGyroscope;

	@Autowired
	Workflow workflow;

	@Test
	public void testFakeGyroscope() {
		Vector3D firstValue = mainGyroscope.getGyroscopeValue();
		Vector3D secondValue = mainGyroscope.getGyroscopeValue();
		assertTrue(firstValue != null && secondValue != null);
		assertTrue(!firstValue.equals(secondValue, 0));
	}

	@Test
	public void test2() {
		workflow.startSample();
		assertTrue(true);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
