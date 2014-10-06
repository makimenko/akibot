package com.akibot.kiss.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.akibot.kiss.component.Component;


public class Client {
	static final Logger log = LogManager.getLogger(Client.class.getName());
	
	private Connection server;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;

	public Client(String IPAddress, int port, Component component) throws IOException {
		log.info("Connecting to server...");
		socket = new Socket(IPAddress, port);
		messages = new LinkedBlockingQueue<Object>();
		server = new Connection(socket, messages);
		log.info("Connected to server");
		
		ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this, messages, component);
		clientMessageHandler.setDaemon(true);
		clientMessageHandler.start();

	}

	public void send(Object obj) {
		server.write(obj);
	}
}
