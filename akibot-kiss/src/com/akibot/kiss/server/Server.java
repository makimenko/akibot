package com.akibot.kiss.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import sun.misc.Cleaner;

public class Server {
	static final Logger log = LogManager.getLogger(Server.class.getName());
	private ConcurrentHashMap<ClientDescription, Connection> clientList;
	private LinkedBlockingQueue<Object> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		log.info("Server starting...");
		clientList = new ConcurrentHashMap<ClientDescription, Connection>();
		messages = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);
		log.info("Server started");

		Thread accept = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket s = serverSocket.accept();
						log.debug("New connection accepted");
						ClientDescription clientDescription = new ClientDescription("X", "Y");
						clientList.putIfAbsent(clientDescription, new Connection(s, messages));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		accept.setDaemon(true);
		accept.start();

		ServerMessageHandler serverMessageHandler = new ServerMessageHandler(this, messages);
		serverMessageHandler.setDaemon(true);
		serverMessageHandler.start();
	}

	public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll(Object message) {
		for (ConcurrentHashMap.Entry<ClientDescription, Connection> entry : clientList.entrySet()) {
			entry.getValue().write(message);
		}

	}

}
