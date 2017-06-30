package com.akibot.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({ MessagingQueueConfiguration.class, WorldConfiguration.class, FakeDeviceConfiguration.class })
@ComponentScan("com.akibot.app.controller")
@PropertySource(value = { "classpath:/akibot.properties" })
public class AkiBotConfiguration {

}
