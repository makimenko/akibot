package com.akibot.kiss.server;

import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.akibot.kiss.message.DistanceMeterCommandMessage;
import com.akibot.kiss.message.DistanceMeterStatusMessage;


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
						DistanceMeterCommandMessage distanceMeterCommandMessage = new DistanceMeterCommandMessage();
						server.sendToAll(distanceMeterCommandMessage);
					}
					
				} else 	if (message instanceof DistanceMeterStatusMessage) {
					DistanceMeterStatusMessage distanceMeterStatusMessage = (DistanceMeterStatusMessage)message;
					log.debug("Distance Received: " + distanceMeterStatusMessage.getMeters()+" meters");
				}	else {
					log.warn("Unknown message received");
				}
			} catch (InterruptedException e) {
			}
		}
	}
	
}
