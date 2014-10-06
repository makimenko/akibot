package com.akibot.kiss.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Client {
	static final Logger log = LogManager.getLogger(Client.class.getName());
	
	private Connection server;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;

	public Client(String IPAddress, int port) throws IOException {
		log.info("Connecting to server...");
		socket = new Socket(IPAddress, port);
		messages = new LinkedBlockingQueue<Object>();
		server = new Connection(socket, messages);
		log.info("Connected to server");
		
		Thread messageHandling = new Thread() {
			public void run() {
				while (true) {
					try {
						Object message = messages.take();
						log.debug("Message taken from queue");
						// Do some handling here...
						log.debug("Message Received: " + message);
					} catch (InterruptedException e) {
						log.warn(e.getMessage());
					}
				}
			}
		};

		messageHandling.setDaemon(true);
		messageHandling.start();
	}

	public void send(Object obj) {
		server.write(obj);
	}
}
