package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.akibot.engine2.message.Message;

public class IncommingMessageManager {

	private BlockingQueue<Message> incomingMessageQueue;
	private IncommingMessageReceiver incommingMessageReceiver;
	private IncommingMessageExecutor incommingMessageExecutor;
	private AkibotClient akibotClient;

	public IncommingMessageManager(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
		this.incomingMessageQueue = new LinkedBlockingQueue<Message>();
		this.incommingMessageReceiver = new IncommingMessageReceiver(akibotClient.getComponent(), akibotClient.getSocket(), incomingMessageQueue);
		this.incommingMessageExecutor = new IncommingMessageExecutor(akibotClient, incomingMessageQueue);
	}

	public void start() {
		incommingMessageReceiver.start();
		incommingMessageExecutor.start();
	}
}
