package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;

public class IncommingMessageManager {
	private static final Logger log = LogManager.getLogger(IncommingMessageManager.class.getName());
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
