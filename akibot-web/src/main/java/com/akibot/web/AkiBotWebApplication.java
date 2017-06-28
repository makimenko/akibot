package com.akibot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.akibot.web.config.WebConfig;

@SpringBootApplication
@Import({ WebConfig.class })
public class AkiBotWebApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AkiBotWebApplication.class, args);
	}

}
