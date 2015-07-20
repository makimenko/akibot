package com.akibot.engine2.network;

import java.util.concurrent.BlockingQueue;

import com.akibot.engine2.exception.ComponentNotReadyException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.message.SystemResponse;

public class IncommingMessageExecutor extends Thread {
	private static final AkiLogger log = AkiLogger.create(IncommingMessageExecutor.class);
	private AkibotClient akibotClient;
	private BlockingQueue<Message> messageQueue;

	public IncommingMessageExecutor(AkibotClient akibotClient, BlockingQueue<Message> messageQueue) {
		this.akibotClient = akibotClient;
		this.messageQueue = messageQueue;
		this.setDaemon(true);
	}

	@Override
	public void run() {

		while (!this.isInterrupted()) {
			try {
				Message message = messageQueue.take();
				if (message instanceof SystemRequest || message instanceof SystemResponse) {
					akibotClient.onSystemMessageReceived(message);
				} else if (!akibotClient.getComponent().getComponentStatus().isReady()) {
					throw new ComponentNotReadyException();
				} else {
					akibotClient.getComponent().onMessageReceived(message);
				}
			} catch (Exception e) {
				log.catching(akibotClient, e);
			}
		}
		log.debug(akibotClient + ": finishing...");
	}

}
