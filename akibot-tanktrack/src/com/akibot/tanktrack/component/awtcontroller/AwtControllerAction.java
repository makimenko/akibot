package com.akibot.tanktrack.component.awtcontroller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public class AwtControllerAction {
	static final Logger log = LogManager.getLogger(AwtControllerAction.class.getName());

	private AkibotClient akibotClient;
	private Integer currentKey;
	private HashMap<Integer, Message> keyMapping;

	public AwtControllerAction(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
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
		akibotClient.getOutgoingMessageManager().broadcastMessage(msg);
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
