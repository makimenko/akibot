package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultServerComponent;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class AkiBotLauncher {
	static final Logger log = LogManager.getLogger(AkiBotLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		String serverHost = "raspberrypi";
		int serverPort = 2000;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		// Server:
		AkibotClient server = new AkibotClient("akibot.server", new DefaultServerComponent(), serverPort);
		server.start();

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new DD1TankTrackComponent(), serverAddress);
		tankTrack.getMyClientDescription().getTopicList().add(new StickMotionRequest());
		// tankTrack.setDaemon(true);
		// Gyroscope:
		// old: 337.0, -106.0, 486.0
		// 2015.01.14: 380.0, 114.5, 397.5
		// AkibotClient gyroscope = new AkibotClient("akibot.gyroscope", new
		// HMC5883LGyroscopeComponent(380.0, 114.5, 397.5, 180), serverAddress);
		// gyroscope.getMyClientDescription().getTopicList().add(new
		// GyroscopeRequest());

		// SpeechSynthesis:
		// String maryttsHost = "192.168.0.102";
		// int maryttsPort = 59125;
		// String maryttsVoice = "cmu-slt-hsmm";
		// SpeechSynthesisComponent speechSynthesisComponent = new
		// SpeechSynthesisComponent(maryttsHost, maryttsPort, maryttsVoice);
		// ClientDescription speechSynthesisDescription = new
		// ClientDescription("akibot.speech.synthesis");
		// speechSynthesisDescription.getTopicList().add(new
		// SpeechSynthesisRequest());
		// Client speechSynthesisClient = new Client(akibotHost, akibotPort,
		// speechSynthesisComponent, speechSynthesisDescription);

		// Distance Meter
		// AkibotClient distance = new AkibotClient("akibot.distance", new
		// DistanceMeterComponent(25, 27, 50000), serverAddress);
		// distance.getMyClientDescription().getTopicList().add(new
		// DistanceRequest());

		// Obstacle:
		// AkibotClient obstacle = new AkibotClient("akibot.obstacle", new
		// ObstacleComponent(), serverAddress);
		// obstacle.getMyClientDescription().getTopicList().add(new
		// ObstacleRequest());
		// obstacle.getMyClientDescription().getTopicList().add(new
		// GyroscopeResponse());
		// obstacle.getMyClientDescription().getTopicList().add(new
		// DistanceResponse());

		// Start all
		tankTrack.start();
		// gyroscope.start();
		// speechSynthesisClient.start();
		// distance.start();
		// obstacle.start();

		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
