package com.akibot.web.config;

import java.net.URI;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingBrokerConfig {

	@Bean
	BrokerService brokerService() throws Exception {
		BrokerService brokerService = BrokerFactory.createBroker(new URI("broker:(tcp://localhost:61616)"));
		brokerService.setPersistent(false);
		brokerService.start();
		return brokerService;
	}

}
