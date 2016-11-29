package com.akibot.app;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

	public MessageConsumer() {
		System.out.println("=========================================!!!!!!!!!!!!!!!!!!! ");
	}
	
	@JmsListener(destination = "sample.queue")
	public void receiveQueue(String text) {
		System.out.println("RECEIVED: " + text);
	}

}
