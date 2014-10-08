package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.Request;

public class ClientMessageHandler extends Thread {
	static final Logger log = LogManager.getLogger(ClientMessageHandler.class.getName());
	private Component component;
	private LinkedBlockingQueue<Object> messages;
	private Client client;

	public ClientMessageHandler(Client client, LinkedBlockingQueue<Object> messages, Component component) {
		this.client = client;
		this.messages = messages;
		this.component = component;
	}

	public void run() {
		while (true) {
			try {
				Object message = messages.take();
				log.debug("Message Received: " + message);
				// Do some handling here...
				if (message instanceof Request) {
					Request request = (Request) message;
					component.executeRequest(client, request);
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
