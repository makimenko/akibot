package com.akibot.web.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akibot.common.constant.AkiBotQueue;

@Configuration
public class WebMessagingQueueConfiguration {

	@Bean
	public Queue queueWorldRequest() {
		return new ActiveMQQueue(AkiBotQueue.QUEUE_WORLD_REQUEST);
	}
}
