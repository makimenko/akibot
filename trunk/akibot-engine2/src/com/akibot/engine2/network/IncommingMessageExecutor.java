package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.message.SystemResponse;

public class IncommingMessageExecutor extends Thread {
	private static final Logger log = LogManager.getLogger(IncommingMessageExecutor.class.getName());
	private BlockingQueue<Message> messageQueue;
	private AkibotClient akibotClient;

	public IncommingMessageExecutor(AkibotClient akibotClient, BlockingQueue<Message> messageQueue) {
		this.akibotClient = akibotClient;
		this.messageQueue = messageQueue;
		this.setDaemon(true);
	}

	public void run() {

		while (!this.isInterrupted()) {
			try {
				Message message = messageQueue.take();
				if (message instanceof SystemRequest || message instanceof SystemResponse) {
					akibotClient.getComponent().onSystemMessageReceived(message);
				} else {
					akibotClient.getComponent().onMessageReceived(message);
				}

			} catch (InterruptedException e) {
				log.catching(e);
			} catch (Exception e) {
				log.catching(e);
			}
		}
	}

}
