package com.akibot.kiss.component.awtcontroller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AwtControllerMouseListener implements MouseListener {
	private Integer codePressed;
	private AwtControllerAction action;

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
		action.action(codePressed);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		action.stop();
	}

}
