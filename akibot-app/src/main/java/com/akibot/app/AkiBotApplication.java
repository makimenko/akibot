package com.akibot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

import com.akibot.app.logic.Workflow;
import com.akibot.common.device.Gyroscope;

@EnableAutoConfiguration
@ImportResource("classpath:ApplicationContext.xml")
public class AkiBotApplication implements CommandLineRunner {

	@Autowired
	private Gyroscope mainGyroscope;

	@Autowired
	private Workflow workflow;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AkiBotApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Value: " + mainGyroscope.getGyroscopeValue());
		System.out.println(workflow);
	}

}
