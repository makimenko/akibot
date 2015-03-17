package com.akibot.tanktrack.component.awtcontroller;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.AkibotClient;

public class AwtControllerAction {

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
	}

	public void setKeyMapping(HashMap<Integer, Message> keyMapping) {
		this.keyMapping = keyMapping;
	}

	public void stop() throws FailedToSendMessageException {
		// if (currentKey != null) {
		if (currentKey != null
				&& (currentKey == KeyEvent.VK_LEFT || currentKey == KeyEvent.VK_RIGHT || currentKey == KeyEvent.VK_UP || currentKey == KeyEvent.VK_DOWN)) {
			if (keyMapping.containsKey(0)) {
				send(0);
			}
		}
		currentKey = null;
	}

}
