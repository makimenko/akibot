package com.akibot.kiss.component.awtcontroller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.message.request.StickMotionRequest;
import com.akibot.kiss.server.Client;
import com.akibot.kiss.types.DirectionType;

public class AwtControllerAppl {
	private Frame mainFrame;
	private TextArea textArea;
	private Client client;

	public AwtControllerAppl(Client client) {
		this.client = client;
		prepareGUI();
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	private void prepareGUI() {
		mainFrame = new Frame("AwtController");
		mainFrame.setSize(900, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		textArea = new TextArea(30, 120);

		GridLayout cursorLayout = new GridLayout(0, 3);
		Panel cursorPanel = new Panel();
		cursorPanel.setLayout(cursorLayout);

		String tankTrackName = "akibot.tanktrack";

		Message messageStop = new StickMotionRequest(DirectionType.STOP);
		Message messageForward = new StickMotionRequest(DirectionType.FORWARD);
		Message messageBackward = new StickMotionRequest(DirectionType.BACKWARD);
		Message messageLeft = new StickMotionRequest(DirectionType.LEFT);
		Message messageRight = new StickMotionRequest(DirectionType.RIGHT);
		Message messageDistanceRequest = new DistanceRequest();
		messageDistanceRequest.setTo("akibot.distance.*");

		messageStop.setTo(tankTrackName);
		messageForward.setTo(tankTrackName);
		messageBackward.setTo(tankTrackName);
		messageLeft.setTo(tankTrackName);
		messageRight.setTo(tankTrackName);

		Button buttonForward = new Button("Forward");
		Button buttonLeft = new Button("Left");
		Button buttonDown = new Button("Backward");
		Button buttonRight = new Button("Right");
		Button buttonDistance = new Button("Distance");

		AwtControllerAction action = new AwtControllerAction(client);
		action.getKeyMapping().put(0, messageStop);
		action.getKeyMapping().put(KeyEvent.VK_LEFT, messageLeft);
		action.getKeyMapping().put(KeyEvent.VK_UP, messageForward);
		action.getKeyMapping().put(KeyEvent.VK_RIGHT, messageRight);
		action.getKeyMapping().put(KeyEvent.VK_DOWN, messageBackward);
		action.getKeyMapping().put(KeyEvent.VK_SPACE, messageDistanceRequest);

		AwtControllerKeyListener keyListener = new AwtControllerKeyListener(action);

		buttonForward.addKeyListener(keyListener);
		buttonDown.addKeyListener(keyListener);
		buttonLeft.addKeyListener(keyListener);
		buttonRight.addKeyListener(keyListener);
		buttonDistance.addKeyListener(keyListener);
		textArea.addKeyListener(keyListener);
		mainFrame.addKeyListener(keyListener);

		buttonForward.addMouseListener(new AwtControllerMouseListener(action, KeyEvent.VK_UP));
		buttonLeft.addMouseListener(new AwtControllerMouseListener(action, KeyEvent.VK_LEFT));
		buttonDown.addMouseListener(new AwtControllerMouseListener(action, KeyEvent.VK_DOWN));
		buttonRight.addMouseListener(new AwtControllerMouseListener(action, KeyEvent.VK_RIGHT));
		buttonDistance.addMouseListener(new AwtControllerMouseListener(action, KeyEvent.VK_SPACE));

		cursorPanel.add(new Label());
		cursorPanel.add(buttonForward);
		cursorPanel.add(new Label());
		cursorPanel.add(buttonLeft);
		cursorPanel.add(buttonDown);
		cursorPanel.add(buttonRight);
		cursorPanel.add(new Label());
		cursorPanel.add(new Label());
		cursorPanel.add(new Label());

		cursorPanel.add(buttonDistance);

		Panel mainPanel = new Panel();

		mainPanel.add(cursorPanel, BorderLayout.EAST);
		mainPanel.add(textArea, BorderLayout.WEST);

		mainFrame.add(mainPanel);
		mainFrame.pack();

		mainFrame.setVisible(true);
	}

	public void start() {

		mainFrame.setVisible(true);
	}

}
