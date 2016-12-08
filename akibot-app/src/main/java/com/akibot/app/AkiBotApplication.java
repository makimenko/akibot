package com.akibot.app;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;

import com.akibot.common.constant.AkiBotQueue;

@EnableAutoConfiguration
@ImportResource("classpath:ApplicationContext.xml")
@ComponentScan("com.akibot.app.consumer")
@EnableJms
public class AkiBotApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(AkiBotApplication.class);

	@Bean
	public Queue queueWorldResponse() {
		return new ActiveMQQueue(AkiBotQueue.QUEUE_WORLD_RESPONSE);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AkiBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		logger.info("Starting AkiBotApplication...");
	}

}
