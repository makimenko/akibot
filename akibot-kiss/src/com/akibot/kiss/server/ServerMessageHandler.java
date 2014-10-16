package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Message;
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

	@Override
	public void run() {
		while (true) {
			try {
				Message message = (Message) messages.take();
				if (message instanceof Request) {
					log.debug("Request: " + message + (message.getTo() == null ? "" : " (to: " + message.getTo() + ")"));
					server.broadcast(message);
				} else if (message instanceof Response) {
					log.debug("Response: " + message + (message.getTo() == null ? "" : " (to: " + message.getTo() + ")"));
					server.broadcast(message);
				} else {
					log.warn("Unknown message received");
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
