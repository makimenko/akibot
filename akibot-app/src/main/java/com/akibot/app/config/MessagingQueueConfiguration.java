package com.akibot.app.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akibot.common.constant.AkiBotQueue;


@Configuration
public class MessagingQueueConfiguration {

	@Bean
	public ActiveMQQueue queueWorldResponse() {
		return new ActiveMQQueue(AkiBotQueue.QUEUE_WORLD_RESPONSE);
	}

	@Bean
	public ActiveMQQueue queueWorldRealtimeEvent() {
		return new ActiveMQQueue(AkiBotQueue.QUEUE_WORLD_REALTIME_EVENT);
	}

}
