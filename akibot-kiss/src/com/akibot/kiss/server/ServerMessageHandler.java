package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.Command;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;

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
						CommandMessage commandMessage = new CommandMessage();
						commandMessage.setCommand(Command.GET_DISTANCE);
						server.sendToAll(commandMessage);
					}
					
				} else 	if (message instanceof DistanceStatusMessage) {
					DistanceStatusMessage distanceStatusMessage = (DistanceStatusMessage)message;
					log.debug("Distance Received: " + distanceStatusMessage.getMeters()+" meters");
				}	else {
					log.warn("Unknown message received");
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
}
