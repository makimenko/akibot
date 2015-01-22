package com.akibot.engine2.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

import com.akibot.engine2.message.Message;

public class IncommingMessageHandler extends Thread {

	private DatagramSocket socket;
	private BlockingQueue<Message> messageQueue;

	public IncommingMessageHandler(DatagramSocket socket, BlockingQueue<Message> messageQueue) {
		this.socket = socket;
		this.messageQueue = messageQueue;
		this.setDaemon(true);
	}

	public void run() {
		byte[] buf = new byte[1000];
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		while (!this.isInterrupted()) {
			try {
				socket.receive(inDatagramPacket);
				Message message = byteToMessage(inDatagramPacket.getData());
				messageQueue.put(message);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Message byteToMessage(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
		ObjectInputStream oos = new ObjectInputStream(baos);
		return (Message) oos.readObject();
	}

}
