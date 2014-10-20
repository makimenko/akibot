package com.akibot.engine.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
	private ObjectInputStream in;
	private MessageReader messageReader;
	private ObjectOutputStream out;

	Connection(Socket socket, LinkedBlockingQueue<Object> messages, Client client) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		messageReader = new MessageReader(in, messages, client);
		messageReader.setDaemon(true);
		messageReader.start();
	}

	public void stop() {
		messageReader.interrupt();
	}

	public void write(Object obj) throws IOException {
		out.writeObject(obj);
	}
}
