package com.akibot.app.logic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ MessagingQueueConfiguration.class, WorldConfiguration.class, FakeDeviceConfiguration.class,
		JmsClientConfig.class })
@ComponentScan("com.akibot.app.controller")
public class AkiBotConfiguration {

}
