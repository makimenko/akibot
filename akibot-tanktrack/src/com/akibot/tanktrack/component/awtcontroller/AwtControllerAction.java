package com.akibot.tanktrack.component.awtcontroller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.exception.FailedToSendMessageException;
import com.akibot.engine.message.Message;
import com.akibot.engine.server.Client;

public class AwtControllerAction {
	static final Logger log = LogManager.getLogger(AwtControllerAction.class.getName());

	private Client client;
	private Integer currentKey;
	private HashMap<Integer, Message> keyMapping;

	public AwtControllerAction(Client client) {
		this.client = client;
		this.currentKey = null;
		this.keyMapping = new HashMap<Integer, Message>();
	}

	public void action(Integer code) throws FailedToSendMessageException {
		if (currentKey == null && keyMapping.containsKey(code)) {
			send(code);
			currentKey = code;
		}
	}

	public HashMap<Integer, Message> getKeyMapping() {
		return keyMapping;
	}

	private void send(Integer code) throws FailedToSendMessageException {
		Message msg = keyMapping.get(code);
		client.send(msg);
		log.debug("SEND: " + msg);
	}

	public void setKeyMapping(HashMap<Integer, Message> keyMapping) {
		this.keyMapping = keyMapping;
	}

	public void stop() throws FailedToSendMessageException {
		if (currentKey != null) {
			if (keyMapping.containsKey(0)) {
				send(0);
			}
			currentKey = null;
		}
	}

}
