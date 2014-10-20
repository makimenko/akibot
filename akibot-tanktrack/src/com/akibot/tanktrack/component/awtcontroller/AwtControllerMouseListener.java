package com.akibot.tanktrack.component.awtcontroller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		action.action(codePressed);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		action.stop();
	}

}
