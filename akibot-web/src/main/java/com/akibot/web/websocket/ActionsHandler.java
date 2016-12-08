package com.akibot.web.websocket;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.akibot.world.message.LookAtOrientationRequest;
import com.akibot.world.message.WorldContentRequest;
import com.akibot.world.message.WorldRequest;
import com.google.gson.Gson;

public class ActionsHandler extends TextWebSocketHandler {
	private Logger logger = LoggerFactory.getLogger(ActionsHandler.class);

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue queueWorldRequest;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		logger.trace("handleTextMessage: " + message.getPayload());
		Gson gson = new Gson();

		WorldRequest worldRequest = gson.fromJson(message.getPayload(), WorldRequest.class);
		String className = worldRequest.getClassName();

		// TODO: Make it more elegant
		if (className.equals(WorldContentRequest.class.getSimpleName())) {
			handleWorldRequest(session, gson.fromJson(message.getPayload(), WorldContentRequest.class));
		} else if (className.equals(LookAtOrientationRequest.class.getSimpleName())) {
			handleWorldRequest(session, gson.fromJson(message.getPayload(), LookAtOrientationRequest.class));
		} else {
			logger.warn("Unsupported request: " + message.getPayload());
		}

	}

	private void handleWorldRequest(WebSocketSession session, WorldRequest worldRequest) {
		logger.trace("handleWorldRequest: " + worldRequest);
		this.jmsMessagingTemplate.convertAndSend(this.queueWorldRequest, worldRequest);
	}

}