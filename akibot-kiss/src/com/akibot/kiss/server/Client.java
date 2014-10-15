package com.akibot.kiss.server;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.Component;

public class Client {
	static final Logger log = LogManager.getLogger(Client.class.getName());

	private Connection server;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;

	public Client(String IPAddress, int port, Component component) throws Exception {
		log.info("Connecting to server...");
		socket = new Socket(IPAddress, port);
		messages = new LinkedBlockingQueue<Object>();

		ClientAuthorization clientAuthorization = new ClientAuthorization(socket, component);
		clientAuthorization.authorize();

		server = new Connection(socket, messages);
		log.info("Connected to server");

		component.setClient(this);
		component.start();
		ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this, messages, component);
		clientMessageHandler.setDaemon(true);
		clientMessageHandler.start();
	}

	public void send(Object obj) {
		server.write(obj);
	}
}
