package com.akibot.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.akibot.app.config.AkiBotConfiguration;
import com.akibot.app.workflow.Workflow;
import com.akibot.common.device.Gyroscope;
import com.akibot.common.element.Vector3D;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("fake")
@Import(AkiBotConfiguration.class)
@EnableJms
@EnableAutoConfiguration
public class AkiBotFakeTest {

	@Autowired
	Gyroscope mainGyroscope;

	@Autowired
	Workflow workflow;

	@Test
	public void testFakeGyroscope() {
		Vector3D firstValue = mainGyroscope.getGyroscopeValue();
		Vector3D secondValue = mainGyroscope.getGyroscopeValue();
		assertNotNull(firstValue);
		assertNotNull(secondValue);
		assertTrue(!firstValue.equals(secondValue, 0));
	}

	@Test
	public void testWorkflow() {
		workflow.run();
		assertTrue(true);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
