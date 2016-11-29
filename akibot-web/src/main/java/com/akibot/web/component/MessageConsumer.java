package com.akibot.web.component;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

//@Component
public class MessageConsumer {

	//@JmsListener(destination = "sample.queue")
	public void receiveQueue(String text) {
		System.out.println("RECEIVED: " + text);
	}

}
