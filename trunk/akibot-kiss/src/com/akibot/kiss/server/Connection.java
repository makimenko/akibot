package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
	private ObjectInputStream in;
	private ObjectOutputStream out;

	Connection(Socket socket, LinkedBlockingQueue<Object> messages, Client client) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		MessageReader read = new MessageReader(in, messages, client);
		read.setDaemon(true);
		read.start();
	}

	public void write(Object obj) {
		try {
			out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
