package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class IncommingMessageManager {
	private static final AkiLogger log = AkiLogger.create(IncommingMessageManager.class);
	private IncommingMessageExecutor incommingMessageExecutor;
	private IncommingMessageReceiver incommingMessageReceiver;
	private BlockingQueue<Message> queue;

	public IncommingMessageManager(AkibotClient akibotClient) {
		this.queue = new LinkedBlockingQueue<Message>();
		this.incommingMessageReceiver = new IncommingMessageReceiver(akibotClient);
		this.incommingMessageExecutor = new IncommingMessageExecutor(akibotClient, queue);
	}

	public BlockingQueue<Message> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<Message> queue) {
		this.queue = queue;
	}

	public void start() {
		incommingMessageReceiver.start();
		incommingMessageExecutor.start();
	}

}
