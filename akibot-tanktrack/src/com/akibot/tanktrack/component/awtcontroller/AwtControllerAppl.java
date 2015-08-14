package com.akibot.tanktrack.component.awtcontroller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationRequest;
import com.akibot.tanktrack.component.obstacle.ObstacleRequest;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.tanktrack.DirectionType;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionRequest;

public class AwtControllerAppl {
	static final AkiLogger log = AkiLogger.create(AwtControllerAppl.class);

	private AkibotClient akibotClient;
	private Frame mainFrame;
	private TextArea textArea;

	public AwtControllerAppl(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
		prepareGUI();
	}

	public TextArea getTextArea() {
		return textArea;
	}

	private void prepareGUI() {
		mainFrame = new Frame("AwtController");
		mainFrame.setSize(900, 500);

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		textArea = new TextArea(50, 120);

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
		Message messageGyroscopeValueRequest = new GyroscopeValueRequest();
		Message messageGyroscopeCalibrationRequest = new GyroscopeCalibrationRequest(30000, 50, false);
		Message messageObstacleRequest = new ObstacleRequest();

		OrientationRequest messageOrientationRequest = new OrientationRequest();
		messageOrientationRequest.setNorthDegreesXY(90);
		messageOrientationRequest.setPrecissionDegrees(1);
		messageOrientationRequest.setTimeoutMillis(10000);

		SpeechSynthesisRequest messageSpeechSynthesis = new SpeechSynthesisRequest("Hello, Michael! Akibot is ready for work. Awaiting your commands.");

		messageDistanceRequest.setTo("akibot.front.distance");
		messageStop.setTo(tankTrackName);
		messageForward.setTo(tankTrackName);
		messageBackward.setTo(tankTrackName);
		messageLeft.setTo(tankTrackName);
		messageRight.setTo(tankTrackName);
		// messageGyroscopeCalibrationRequest.setTo("akibot.gyroscope");

		Button buttonForward = new Button("Forward");
		Button buttonLeft = new Button("Left");
		Button buttonDown = new Button("Backward");
		Button buttonRight = new Button("Right");
		Button buttonDistance = new Button("Distance");
		Button buttonGyroscope = new Button("Gyroscope");
		Button buttonOrientation = new Button("Orientation");
		Button buttonSpeechSynthesis = new Button("Speech Synthesis");
		Button buttonGyroscopeCalibrationRequest = new Button("Gyroscope Calibration");
		Button buttonObstacleRequest = new Button("Obstacle");
		Button buttonTimedMotionRequest = new Button("Forward (1 sec)");
		buttonTimedMotionRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Response response = null;
				try {
					TimedMotionRequest messageTimedMotionRequest = new TimedMotionRequest();
					messageTimedMotionRequest.setDirectionType(DirectionType.FORWARD);
					messageTimedMotionRequest.setMilliseconds(1000);

					response = akibotClient.getOutgoingMessageManager().sendSyncRequest(messageTimedMotionRequest, 2000);
					textArea.append("SYNC RESPONSE: " + response + "\n");

				} catch (FailedToSendMessageException e1) {
					log.catching(akibotClient, e1);
				}
			}
		});

		Button buttonServoCenterRequest = new Button("Servo Center");
		buttonServoCenterRequest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Response response = null;
				try {
					ServoRequest servoFrontBaseRequest = new ServoRequest();
					servoFrontBaseRequest.setMicroseconds(500000);
					servoFrontBaseRequest.setValue(14);

					ServoRequest servoFrontHeadRequest = new ServoRequest();
					servoFrontHeadRequest.setMicroseconds(500000);
					servoFrontHeadRequest.setValue(14);

					servoFrontBaseRequest.setTo("akibot.servo.back.base");
					servoFrontHeadRequest.setTo("akibot.servo.back.head");
					akibotClient.getOutgoingMessageManager().broadcastMessage(servoFrontBaseRequest);
					Thread.sleep(1000);
					akibotClient.getOutgoingMessageManager().broadcastMessage(servoFrontHeadRequest);
					Thread.sleep(1000);

					servoFrontBaseRequest.setTo("akibot.servo.front.base");
					servoFrontHeadRequest.setTo("akibot.servo.front.head");
					akibotClient.getOutgoingMessageManager().broadcastMessage(servoFrontBaseRequest);
					Thread.sleep(1000);
					akibotClient.getOutgoingMessageManager().broadcastMessage(servoFrontHeadRequest);
					Thread.sleep(1000);

				} catch (FailedToSendMessageException e1) {
					log.catching(akibotClient, e1);
				} catch (InterruptedException e1) {
					log.catching(akibotClient, e1);
				}
			}
		});

		Button buttonSync = new Button("Sync Request");
		buttonSync.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Response response = null;
				try {
					long startTime = System.currentTimeMillis();
					DistanceRequest distanceRequest = new DistanceRequest();
					distanceRequest.setTo("akibot.front.distance");

					for (int i = 1; i <= 10; i++) {
						response = akibotClient.getOutgoingMessageManager().sendSyncRequest(distanceRequest, 1000);
						textArea.append("SYNC RESPONSE: " + response + "\n");
					}

					textArea.append("Total ms: " + (System.currentTimeMillis() - startTime) + "\n");
				} catch (FailedToSendMessageException e1) {
					log.catching(akibotClient, e1);
				}
			}
		});

		AwtControllerAction directionAction = new AwtControllerAction(akibotClient);
		directionAction.getKeyMapping().put(0, messageStop);
		directionAction.getKeyMapping().put(KeyEvent.VK_LEFT, messageLeft);
		directionAction.getKeyMapping().put(KeyEvent.VK_UP, messageForward);
		directionAction.getKeyMapping().put(KeyEvent.VK_RIGHT, messageRight);
		directionAction.getKeyMapping().put(KeyEvent.VK_DOWN, messageBackward);

		directionAction.getKeyMapping().put(KeyEvent.VK_1, messageGyroscopeValueRequest);
		directionAction.getKeyMapping().put(KeyEvent.VK_2, messageDistanceRequest);
		directionAction.getKeyMapping().put(KeyEvent.VK_3, messageOrientationRequest);
		directionAction.getKeyMapping().put(KeyEvent.VK_4, messageSpeechSynthesis);
		directionAction.getKeyMapping().put(KeyEvent.VK_5, messageGyroscopeCalibrationRequest);
		directionAction.getKeyMapping().put(KeyEvent.VK_6, messageObstacleRequest);

		AwtControllerKeyListener directionKeyListener = new AwtControllerKeyListener(directionAction);
		buttonForward.addKeyListener(directionKeyListener);
		buttonDown.addKeyListener(directionKeyListener);
		buttonLeft.addKeyListener(directionKeyListener);
		buttonRight.addKeyListener(directionKeyListener);
		buttonGyroscope.addKeyListener(directionKeyListener);
		buttonDistance.addKeyListener(directionKeyListener);
		buttonOrientation.addKeyListener(directionKeyListener);
		buttonSpeechSynthesis.addKeyListener(directionKeyListener);
		buttonGyroscopeCalibrationRequest.addKeyListener(directionKeyListener);
		buttonObstacleRequest.addKeyListener(directionKeyListener);

		buttonForward.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_UP));
		buttonLeft.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_LEFT));
		buttonDown.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_DOWN));
		buttonRight.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_RIGHT));
		buttonGyroscope.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_1));
		buttonDistance.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_2));
		buttonOrientation.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_3));
		buttonSpeechSynthesis.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_4));
		buttonGyroscopeCalibrationRequest.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_5));
		buttonObstacleRequest.addMouseListener(new AwtControllerMouseListener(directionAction, KeyEvent.VK_6));

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
		cursorPanel.add(buttonSync);
		cursorPanel.add(buttonGyroscope);
		cursorPanel.add(buttonOrientation);
		cursorPanel.add(buttonSpeechSynthesis);
		cursorPanel.add(buttonGyroscopeCalibrationRequest);
		cursorPanel.add(buttonObstacleRequest);
		cursorPanel.add(buttonTimedMotionRequest);
		cursorPanel.add(buttonServoCenterRequest);

		Panel mainPanel = new Panel();

		mainPanel.add(cursorPanel, BorderLayout.EAST);
		mainPanel.add(textArea, BorderLayout.WEST);

		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	public void start() {

		mainFrame.setVisible(true);
	}

}
