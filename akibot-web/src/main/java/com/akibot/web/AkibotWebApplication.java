package com.akibot.web;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@EnableAutoConfiguration
@SpringBootApplication
@EnableJms
public class AkibotWebApplication {

	@Bean
	public Queue queue() {
		return new ActiveMQQueue("sample.queue");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AkibotWebApplication.class, args);
		
		
	}
	
	

}
