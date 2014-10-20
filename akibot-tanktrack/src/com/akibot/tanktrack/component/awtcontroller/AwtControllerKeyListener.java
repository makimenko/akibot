package com.akibot.tanktrack.component.awtcontroller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AwtControllerKeyListener implements KeyListener {
	private AwtControllerAction action;
	private Integer currentKey;

	public AwtControllerKeyListener(AwtControllerAction action) {
		this.action = action;
		currentKey = null;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (currentKey == null) {
			currentKey = e.getKeyCode();
			action.action(currentKey);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (currentKey != null && currentKey == e.getKeyCode()) {
			action.stop();
			currentKey = null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
