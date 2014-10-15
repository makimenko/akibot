package com.akibot.kiss.component.awtcontroller;

import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.server.Client;

public class AwtControllerMouseListener implements MouseListener {
	private Message messagePressed;
	private Message messageReleased;
	private TextArea textArea;
	private Client client;

	public AwtControllerMouseListener(Client client, Message messagePressed, Message messageReleased, TextArea textArea) {
		this.client = client;
		this.messagePressed = messagePressed;
		this.messageReleased = messageReleased;
		this.textArea = textArea;
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
		if (messagePressed != null) {
			textArea.append(messagePressed + "\n");
			client.send(messagePressed);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (messageReleased != null) {
			textArea.append(messageReleased + "\n");
			client.send(messageReleased);
		}
	}

}
