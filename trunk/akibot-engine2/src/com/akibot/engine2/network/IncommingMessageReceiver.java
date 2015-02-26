package com.akibot.engine2.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.Component;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;

public class IncommingMessageReceiver extends Thread {
	private static final Logger log = LogManager.getLogger(IncommingMessageReceiver.class.getName());
	private DatagramSocket socket;
	private BlockingQueue<Message> messageQueue;
	private Component component;

	public IncommingMessageReceiver(Component component, DatagramSocket socket, BlockingQueue<Message> messageQueue) {
		this.component = component;
		this.socket = socket;
		this.messageQueue = messageQueue;
		this.setDaemon(true);
	}

	public void run() {
		byte[] buf = new byte[10000]; // TODO: ? is it enough?
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		while (!this.isInterrupted()) {
			try {
				socket.receive(inDatagramPacket);
				Message message = byteToMessage(inDatagramPacket.getData());

				if (message instanceof Response && component.getSyncId() != null && ((Response) message).getSyncId() != null
						&& ((Response) message).getSyncId().equals(component.getSyncId())) {
					log.trace(component + ": Sync Message Received: " + message + " (" + ((Response) message).getSyncId() + ")");
					component.setSyncResponse((Response) message);
					synchronized (component.getSyncId()) {
						component.getSyncId().notify();
					}
				} else {
					messageQueue.put(message);
				}

			} catch (IOException e) {
				log.catching(e);
			} catch (ClassNotFoundException e) {
				log.catching(e);
			} catch (InterruptedException e) {
				log.catching(e);
			}
		}
	}

	private Message byteToMessage(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
		ObjectInputStream oos = new ObjectInputStream(baos);
		return (Message) oos.readObject();
	}

}
