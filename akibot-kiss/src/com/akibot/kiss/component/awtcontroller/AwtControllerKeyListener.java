package com.akibot.kiss.component.awtcontroller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.server.Client;

public class AwtControllerKeyListener implements KeyListener {
	private HashMap<Integer, Message> keyMapping;
	private Client client;
	private boolean pressed;

	public AwtControllerKeyListener(Client client) {
		this.client = client;
		pressed = false;
		setKeyMapping(new HashMap<Integer, Message>());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!pressed) {
			Integer keyCode = (Integer) e.getKeyCode();
			if (keyMapping.containsKey(keyCode)) {
				Message msg = keyMapping.get(keyCode);
				client.send(msg);
				System.out.println("START: " + msg);
				pressed = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (pressed) {
			Message msg = keyMapping.get(0);
			client.send(msg);
			System.out.println("RELEASED: " + msg);
			pressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public HashMap<Integer, Message> getKeyMapping() {
		return keyMapping;
	}

	public void setKeyMapping(HashMap<Integer, Message> keyMapping) {
		this.keyMapping = keyMapping;
	}

}
