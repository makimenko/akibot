package com.akibot.app.logic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.akibot.common.device.Gyroscope;
import com.akibot.device.fake.gyroscope.FakeGyroscope;

@Configuration
@Profile("fake")
public class FakeDeviceConfiguration {

	@Bean
	public Gyroscope mainGyroscope() {
		return new FakeGyroscope();
	}

}
