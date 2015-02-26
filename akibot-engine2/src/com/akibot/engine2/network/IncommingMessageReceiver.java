package com.akibot.engine2.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;

public class IncommingMessageReceiver extends Thread {
	private static final Logger log = LogManager.getLogger(IncommingMessageReceiver.class.getName());
	private AkibotClient akibotClient;
	private final int BUFFER_SIZE = 10000; // TODO: ? is it enough?
	private DatagramSocket socket;

	public IncommingMessageReceiver(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
		this.socket = akibotClient.getSocket();
		this.setDaemon(true);
	}

	private Message byteToMessage(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
		ObjectInputStream oos = new ObjectInputStream(baos);
		return (Message) oos.readObject();
	}

	public void run() {
		byte[] buf = new byte[BUFFER_SIZE];
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		while (!this.isInterrupted()) {
			try {
				socket.receive(inDatagramPacket);
				Message message = byteToMessage(inDatagramPacket.getData());

				SynchronizedMessageManager sync = akibotClient.getSynchronizedMessageManager();
				if (message instanceof Response && sync.getSyncId() != null && ((Response) message).getSyncId() != null
						&& ((Response) message).getSyncId().equals(sync.getSyncId())) {
					log.trace(akibotClient + ": Sync Message Received: " + message + " (" + ((Response) message).getSyncId() + ")");
					sync.setSyncResponse((Response) message);
					synchronized (sync.getSyncId()) {
						sync.getSyncId().notify();
					}
				} else {
					akibotClient.getIncommingMessageManager().getQueue().put(message);
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

}
