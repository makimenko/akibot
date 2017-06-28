package com.akibot.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.akibot.app.logic.config.AkiBotConfiguration;

@EnableAutoConfiguration
@Import(AkiBotConfiguration.class)
public class AkiBotApplication implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(AkiBotApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AkiBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		logger.info("Starting AkiBotApplication...");
	}

}
