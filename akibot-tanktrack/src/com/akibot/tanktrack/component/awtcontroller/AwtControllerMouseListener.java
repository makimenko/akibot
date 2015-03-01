package com.akibot.tanktrack.component.awtcontroller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.akibot.engine2.exception.FailedToSendMessageException;

public class AwtControllerMouseListener implements MouseListener {
	private AwtControllerAction action;
	private Integer codePressed;

	public AwtControllerMouseListener(AwtControllerAction action, Integer codePressed) {
		this.action = action;
		this.codePressed = codePressed;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		try {
			action.action(codePressed);
		} catch (FailedToSendMessageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			action.stop();
		} catch (FailedToSendMessageException e1) {
			e1.printStackTrace();
		}
	}

}
