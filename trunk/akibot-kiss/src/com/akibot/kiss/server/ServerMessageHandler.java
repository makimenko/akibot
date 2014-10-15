package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Request;
import com.akibot.kiss.message.Response;

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

				if (message instanceof Request) {
					log.debug("Request: " + message);
					server.broadcast(message);
				} else if (message instanceof Response) {
					log.debug("Response: " + message);

					// DistanceResponse distanceResponse = (DistanceResponse)
					// message;
					// log.debug("Distance Received: " +
					// distanceResponse.getMeters() + " meters");
				} else {
					log.warn("Unknown message received");
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
