package com.akibot.kiss.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.akibot.kiss.message.Command;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;

public class Server {
	static final Logger log = LogManager.getLogger(Server.class.getName());
	private ArrayList<Connection> clientList;
	private LinkedBlockingQueue<Object> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		log.info("Server starting...");		
		clientList = new ArrayList<Connection>();
		messages = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);
		log.info("Server started");	
		
		Thread accept = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket s = serverSocket.accept();
						log.debug("New connection accepted");
						clientList.add(new Connection(s, messages));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		accept.setDaemon(true);
		accept.start();

		Thread messageHandling = new Thread() {
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
								sendToAll(commandMessage);
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
		};

		messageHandling.setDaemon(true);
		messageHandling.start();
	}

	public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll(Object message) {
		for (Connection client : clientList)
			client.write(message);
	}

}
