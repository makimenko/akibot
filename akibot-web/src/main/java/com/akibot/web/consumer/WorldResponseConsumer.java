package com.akibot.web.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;

import com.akibot.common.constant.AkiBotQueue;
import com.akibot.world.message.WorldResponse;

@Controller
public class WorldResponseConsumer {
	private Logger logger = LoggerFactory.getLogger(WorldResponseConsumer.class);

	public WorldResponseConsumer() {
		logger.info("Starting WorldResponseConsumer...");
	}

	@JmsListener(destination = AkiBotQueue.QUEUE_WORLD_RESPONSE)
	public void receiveWorldResponse(WorldResponse worldResponse) {
		logger.trace("receiveWorldResponse: " + worldResponse);

	}

}
