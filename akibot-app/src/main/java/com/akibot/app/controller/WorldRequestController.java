package com.akibot.app.controller;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.akibot.common.constant.AkiBotQueue;
import com.akibot.world.dao.WorldContentDao;
import com.akibot.world.message.WorldContentResponse;
import com.akibot.world.message.WorldRequest;

@Controller
public class WorldRequestController {

	private Logger logger = LoggerFactory.getLogger(WorldRequestController.class);

	@Autowired
	private WorldContentDao worldContentDao;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queueWorldResponse;

	public WorldRequestController() {
		logger.info("Starting WorldRequestConsumer...");
	}

	@JmsListener(destination = AkiBotQueue.QUEUE_WORLD_REQUEST)
	public void receiveWorldRequest(WorldRequest worldRequest) {
		logger.trace("receiveWorldRequest: " + worldRequest);

		WorldContentResponse worldContentResponse = new WorldContentResponse();
		worldContentResponse.setWorldNode(worldContentDao.getWorldNode());

		this.jmsMessagingTemplate.convertAndSend(this.queueWorldResponse, worldContentResponse);
	}

}
