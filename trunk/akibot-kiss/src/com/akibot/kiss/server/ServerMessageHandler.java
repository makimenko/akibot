package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Message;

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
				log.debug("Message: "
						+ message
						+ (message.getTo() == null ? "" : " (from: " + message.getFrom() + ", to: " + message.getTo() + ", syncId="
								+ message.getSyncId() + ")"));
				server.broadcast(message);
			} catch (InterruptedException e) {
				// TODO:
			}
		}
	}

}
