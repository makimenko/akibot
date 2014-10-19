package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Response;

public class MessageReader extends Thread {
	static final Logger log = LogManager.getLogger(MessageReader.class.getName());
	private LinkedBlockingQueue<Object> messages;
	private ObjectInputStream in;
	private Client client;

	public MessageReader(ObjectInputStream in, LinkedBlockingQueue<Object> messages, Client client) {
		this.messages = messages;
		this.in = in;
		this.client = client;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object obj = in.readObject();
				if (obj instanceof Response && client != null) {
					Response response = (Response) obj;
					log.debug("Response: " + response + " - " + response.getSyncId() + " / " + client.getSyncId());
				}

				if (obj instanceof Response && client != null && client.getSyncId() != null
						&& ((Response) obj).getSyncId().equals(client.getSyncId())) {
					log.debug("Sync Message Received: " + ((Response) obj).getSyncId() + ": " + obj);
					client.setSyncResponse((Response) obj);
					synchronized (client.getSyncId()) {
						client.getSyncId().notify();
					}
				} else {

					messages.put(obj);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}

}
