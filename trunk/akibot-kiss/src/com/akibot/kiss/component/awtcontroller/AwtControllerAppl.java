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

import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.server.Client;

public class AwtControllerAppl {
	private Frame mainFrame;
	private TextArea textArea;
	private Client client;

	public AwtControllerAppl(Client client) {
		this.client = client;
		prepareGUI();
	}

	private void prepareGUI() {
		mainFrame = new Frame("AwtController");
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		textArea = new TextArea(30,100);

		GridLayout cursorLayout = new GridLayout(0, 3);
		Panel cursorPanel = new Panel();
		cursorPanel.setLayout(cursorLayout);

		Button buttonUp = new Button("Up");
		buttonUp.addMouseListener(new AwtControllerMouseListener(client, new DistanceRequest(), null, textArea));

		Button buttonLeft = new Button("Left");
		// buttonLeft.addMouseListener(new AwtControllerMouseListener("LEFT",
		// textArea));

		Button buttonDown = new Button("");
		// buttonDown.addMouseListener(new AwtControllerMouseListener("DOWN",
		// textArea));

		Button buttonRight = new Button("");
		// buttonRight.addMouseListener(new AwtControllerMouseListener("RIGHT",
		// textArea));

		cursorPanel.add(new Label());
		cursorPanel.add(buttonUp);
		cursorPanel.add(new Label());
		cursorPanel.add(buttonLeft);
		cursorPanel.add(buttonDown);
		cursorPanel.add(buttonRight);

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
