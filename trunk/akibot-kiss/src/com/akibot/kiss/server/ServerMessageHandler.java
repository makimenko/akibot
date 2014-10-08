package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.message.response.DistanceResponse;

public class ServerMessageHandler extends Thread {
	static final Logger log = LogManager.getLogger(ServerMessageHandler.class.getName());
	private Server server;
	LinkedBlockingQueue<Object> messages;

	public ServerMessageHandler(Server server, LinkedBlockingQueue<Object> messages) {
		this.server = server;
		this.messages = messages;
	}

	public void run() {
		while (true) {
			try {
				Object message = messages.take();
				log.debug("New message taken");

				if (message instanceof String) {
					log.debug("Message Received: " + message);
					if (((String) message).equalsIgnoreCase("X")) {
						DistanceRequest distanceRequest = new DistanceRequest();
						server.sendToAll(distanceRequest);
					}

				} else if (message instanceof DistanceResponse) {
					DistanceResponse distanceResponse = (DistanceResponse) message;
					log.debug("Distance Received: " + distanceResponse.getMeters() + " meters");
				} else {
					log.warn("Unknown message received");
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
