package com.akibot.kiss.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
	private Connection server;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;

	public Client(String IPAddress, int port) throws IOException {
		socket = new Socket(IPAddress, port);
		messages = new LinkedBlockingQueue<Object>();
		server = new Connection(socket, messages);

		Thread messageHandling = new Thread() {
			public void run() {
				while (true) {
					try {
						Object message = messages.take();
						// Do some handling here...
						System.out.println("Message Received: " + message);
					} catch (InterruptedException e) {
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
