package com.akibot.kiss.component.awtcontroller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.server.Client;

public class AwtControllerAction {
	static final Logger log = LogManager.getLogger(AwtControllerAction.class.getName());

	private HashMap<Integer, Message> keyMapping;
	private Client client;
	private Integer currentKey;

	public AwtControllerAction(Client client) {
		this.client = client;
		this.currentKey = null;
		this.keyMapping = new HashMap<Integer, Message>();
	}

	public HashMap<Integer, Message> getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(HashMap<Integer, Message> keyMapping) {
		this.keyMapping = keyMapping;
	}

	private void send(Integer code) {
		Message msg = keyMapping.get(code);
		client.send(msg);
		log.debug("SEND: " + msg);
	}

	public void stop() {
		if (currentKey != null) {
			send(0);
			currentKey = null;
		}
	}

	public void action(Integer code) {
		if (currentKey == null && keyMapping.containsKey(code)) {
			send(code);
			currentKey = code;
		}
	}

}
