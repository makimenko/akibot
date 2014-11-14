package com.akibot.engine.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.message.Response;

public class MessageReader extends Thread {
	static final Logger log = LogManager.getLogger(MessageReader.class.getName());
	private Client client;
	private ObjectInputStream in;
	private LinkedBlockingQueue<Object> messages;

	public MessageReader(ObjectInputStream in, LinkedBlockingQueue<Object> messages, Client client) {
		this.messages = messages;
		this.in = in;
		this.client = client;
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			try {
				Object obj = in.readObject();
				if (obj instanceof Response && client != null && client.getSyncId() != null && ((Response) obj).getSyncId() != null
						&& ((Response) obj).getSyncId().equals(client.getSyncId())) {
					log.trace("Sync Message Received: " + obj + " (" + ((Response) obj).getSyncId() + ")");
					client.setSyncResponse((Response) obj);
					synchronized (client.getSyncId()) {
						client.getSyncId().notify();
					}
				} else {
					messages.put(obj);
				}
			} catch (IOException e) {
				break;
			} catch (InterruptedException e) {
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
