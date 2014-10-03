package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
	private ArrayList<Connection> clientList;
	private LinkedBlockingQueue<Object> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		clientList = new ArrayList<Connection>();
		messages = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);

		Thread accept = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket s = serverSocket.accept();
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

						if (((String) message).equalsIgnoreCase("X")) {
							System.out.println("Broadcasting: " + message);
							sendToAll("HEY!!! broadcasting X!");
						}
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

	public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll(Object message) {
		for (Connection client : clientList)
			client.write(message);
	}

}
