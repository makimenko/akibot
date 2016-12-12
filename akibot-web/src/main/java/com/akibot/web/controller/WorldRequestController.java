package com.akibot.web.controller;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.akibot.world.message.WorldRequest;

@Controller
public class WorldRequestController {
	private Logger logger = LoggerFactory.getLogger(WorldRequestController.class);

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queueWorldRequest;

	@MessageMapping(value = "/worldRequest")
	public void worldRequest(WorldRequest worldRequest) {
		logger.trace("worldRequest: " + worldRequest.getClassName());
		this.jmsMessagingTemplate.convertAndSend(this.queueWorldRequest, worldRequest);
	}

}