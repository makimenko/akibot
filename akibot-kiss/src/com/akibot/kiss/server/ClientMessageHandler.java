package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.Message;

public class ClientMessageHandler extends Thread {
	static final Logger log = LogManager.getLogger(ClientMessageHandler.class.getName());
	private Client client;
	private Component component;
	private LinkedBlockingQueue<Object> messages;

	public ClientMessageHandler(Client client, LinkedBlockingQueue<Object> messages, Component component) {
		this.client = client;
		this.messages = messages;
		this.component = component;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Message message = (Message) messages.take();
				log.debug(client.getClientDescription().getName() + " - Received: " + message
						+ (message.getFrom() == null ? "" : " (from " + message.getFrom() + ")"));

				if (message instanceof Message) {
					component.processMessage((Message) message);
				}
			} catch (InterruptedException e) {
				log.warn(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
