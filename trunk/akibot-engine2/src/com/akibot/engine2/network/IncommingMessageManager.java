package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;

public class IncommingMessageManager {
	private static final Logger log = LogManager.getLogger(IncommingMessageManager.class.getName());
	private BlockingQueue<Message> incomingMessageQueue;
	private IncommingMessageReceiver incommingMessageReceiver;
	private IncommingMessageExecutor incommingMessageExecutor;

	public IncommingMessageManager(AkibotClient akibotClient) {
		this.incomingMessageQueue = new LinkedBlockingQueue<Message>();
		this.incommingMessageReceiver = new IncommingMessageReceiver(akibotClient.getComponent(), akibotClient.getSocket(), incomingMessageQueue);
		this.incommingMessageExecutor = new IncommingMessageExecutor(akibotClient, incomingMessageQueue);
	}

	public void start() {
		incommingMessageReceiver.start();
		incommingMessageExecutor.start();
	}
}
