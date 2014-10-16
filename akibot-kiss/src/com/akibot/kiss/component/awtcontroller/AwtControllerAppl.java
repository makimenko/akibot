package com.akibot.kiss.component.awtcontroller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
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
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		textArea = new TextArea(30, 100);

		GridLayout cursorLayout = new GridLayout(0, 3);
		Panel cursorPanel = new Panel();
		cursorPanel.setLayout(cursorLayout);

		String tankTrackName = "akibot.tanktrack";

		Message messageStop = new StickMotionRequest(DirectionType.STOP);
		Message messageForward = new StickMotionRequest(DirectionType.FORWARD);
		Message messageBackward = new StickMotionRequest(DirectionType.BACKWARD);
		Message messageLeft = new StickMotionRequest(DirectionType.LEFT);
		Message messageRight = new StickMotionRequest(DirectionType.RIGHT);

		messageStop.setTo(tankTrackName);
		messageForward.setTo(tankTrackName);
		messageBackward.setTo(tankTrackName);
		messageLeft.setTo(tankTrackName);
		messageRight.setTo(tankTrackName);

		Button buttonForward = new Button("Forward");
		Button buttonLeft = new Button("Left");
		Button buttonDown = new Button("Backward");
		Button buttonRight = new Button("Right");

		buttonForward.addMouseListener(new AwtControllerMouseListener(client, messageForward, messageStop, textArea));
		buttonLeft.addMouseListener(new AwtControllerMouseListener(client, messageLeft, messageStop, textArea));
		buttonDown.addMouseListener(new AwtControllerMouseListener(client, messageBackward, messageStop, textArea));
		buttonRight.addMouseListener(new AwtControllerMouseListener(client, messageRight, messageStop, textArea));

		Button buttonDistance = new Button("Distance");
		Message messagePressed = new DistanceRequest();
		messagePressed.setTo("akibot.distance.*");
		buttonDistance.addMouseListener(new AwtControllerMouseListener(client, messagePressed, null, textArea));

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
