package com.akibot.kiss.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.Message;

public class ComponentController {

	private Component component;
	private Socket serverSocket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	public ComponentController(String host, int port, Component component) throws UnknownHostException, IOException {
		this.component = component;
		this.component.setComponentController(this);

		this.serverSocket = new Socket(host, port);

		InputStream inputStream = this.serverSocket.getInputStream();
		OutputStream outputStream = this.serverSocket.getOutputStream();

		this.objectInputStream = new ObjectInputStream(inputStream);
		this.objectOutputStream = new ObjectOutputStream(outputStream);

	}

	public void receive(Message message) throws Exception {
		if (message instanceof CommandMessage) {
			component.executeCommand((CommandMessage) message);
		}
	}

	public void send(Message message) throws IOException {
		objectOutputStream.writeObject(message);
	}

	public void run() throws Exception {
		Object object;
		while ((object = objectInputStream.readObject()) != null) {
			
			if (object instanceof CommandMessage) {
				CommandMessage command = (CommandMessage) object;
				System.out.println("ComponentController: CommandMessage");
				component.executeCommand(command);
			} else {
				System.out.println("ComponentController: unknown request");
			}
		}

	}

}
