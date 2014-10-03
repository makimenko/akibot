package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Connection {
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;

	Connection(Socket socket, LinkedBlockingQueue<Object> messages) throws IOException {
		this.socket = socket;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());

		MessageReader read = new MessageReader(in, messages);
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
