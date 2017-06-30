package com.akibot.web.controller.outside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.akibot.common.constant.AkiBotQueue;
import com.akibot.web.config.WebConstants;
import com.akibot.world.message.WorldRealtimeEvent;
import com.akibot.world.message.WorldResponse;

@Controller
public class WorldResponseController {
	
	private Logger logger = LoggerFactory.getLogger(WorldResponseController.class);
	
	private SimpMessagingTemplate template;

	@Autowired
	public WorldResponseController(SimpMessagingTemplate template) {
		this.template = template;
	}

	@JmsListener(destination = AkiBotQueue.QUEUE_WORLD_RESPONSE)
	public void receiveWorldResponse(WorldResponse worldResponse) {
		logger.trace("receiveWorldResponse: " + worldResponse);
		this.template.convertAndSend(WebConstants.TOPIC_WORLD_RESPONSE, worldResponse);
	}

	@JmsListener(destination = AkiBotQueue.QUEUE_WORLD_REALTIME_EVENT)
	public void receiveWorldRealtimeEvent(WorldRealtimeEvent worldRealtimeEvent) {
		logger.trace("receiveWorldRealtimeEvent: " + worldRealtimeEvent);
		this.template.convertAndSend(WebConstants.TOPIC_WORLD_RESPONSE, worldRealtimeEvent);
	}

}
