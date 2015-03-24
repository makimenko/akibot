package com.akibot.tanktrack.component.awtcontroller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;

public class AwtControllerKeyListener implements KeyListener {
	static final AkiLogger log = AkiLogger.create(AwtControllerKeyListener.class);

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
			try {
				action.action(currentKey);
			} catch (FailedToSendMessageException e1) {
				log.catching(e1);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (currentKey != null && currentKey == e.getKeyCode()) {
			try {
				action.stop();
			} catch (FailedToSendMessageException e1) {
				log.catching(e1);
			}
			currentKey = null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
