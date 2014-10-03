package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageReader extends Thread {
	private LinkedBlockingQueue<Object> messages;
	private ObjectInputStream in;
	
	public MessageReader(ObjectInputStream in, LinkedBlockingQueue<Object> messages) {
		this.messages = messages;
		this.in = in;
	}
	
	public void run() {
		while (true) {
			try {
				Object obj = in.readObject();
				System.out.println("Client.readObject");
				messages.put(obj);
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
