package com.akibot.engine2.server;

import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;

import com.akibot.engine2.message.Message;

public class MessageQueueHandler extends Thread {

	private BlockingQueue<Message> messageQueue;
	private AkibotNode akibotNode;

	public MessageQueueHandler(AkibotNode akibotNode, BlockingQueue<Message> messageQueue) {
		this.akibotNode = akibotNode;
		this.messageQueue = messageQueue;
		this.setDaemon(true);
	}

	public void run() {
		byte[] buf = new byte[1000];
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		while (!this.isInterrupted()) {
			try {
				Message message = messageQueue.take();
				akibotNode.onMessageReceived(message);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
