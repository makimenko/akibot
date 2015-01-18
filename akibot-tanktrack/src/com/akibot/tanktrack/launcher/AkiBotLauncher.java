package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.engine.server.Server;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeComponent;
import com.akibot.tanktrack.component.gyroscope.GyroscopeRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.obstacle.ObstacleComponent;
import com.akibot.tanktrack.component.obstacle.ObstacleRequest;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;

public class AkiBotLauncher {
	static final Logger log = LogManager.getLogger(AkiBotLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		String akibotHost = "localhost";
		int akibotPort = 2000;

		// Server:
		Server server = new Server(akibotPort);
		server.start();

		// TankTrack:
		TankTrackComponent tankTrackComponent = new DD1TankTrackComponent();
		ClientDescription tankTrackDescription = new ClientDescription("akibot.tanktrack");
		tankTrackDescription.getTopicList().add(new StickMotionRequest());
		Client tankClient = new Client(akibotHost, akibotPort, tankTrackComponent, tankTrackDescription);

		// Gyroscope:
		// old: 337.0, -106.0, 486.0
		// 2015.01.14: 380.0, 114.5, 397.5
		GyroscopeComponent gyroscopeComponent = new HMC5883LGyroscopeComponent(380.0, 114.5, 397.5, 180);
		ClientDescription gyroscopeClientDescription = new ClientDescription("akibot.gyroscope");
		gyroscopeClientDescription.getTopicList().add(new GyroscopeRequest());
		Client gyroscopeClient = new Client(akibotHost, akibotPort, gyroscopeComponent, gyroscopeClientDescription);

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

		DistanceMeterComponent distanceMeterComponent = new DistanceMeterComponent(25, 27, 50000);
		ClientDescription distanceMeterClientDescription = new ClientDescription("akibot.distance");
		distanceMeterClientDescription.getTopicList().add(new DistanceRequest());
		Client distanceMeteriClient = new Client(akibotHost, akibotPort, distanceMeterComponent, distanceMeterClientDescription);

		ObstacleComponent obstacleComponent = new ObstacleComponent();
		ClientDescription obstacleDescription = new ClientDescription("akibot.obstacle");
		obstacleDescription.getTopicList().add(new ObstacleRequest());
		obstacleDescription.getTopicList().add(new GyroscopeResponse());
		obstacleDescription.getTopicList().add(new DistanceResponse());
		Client obstacleClient = new Client(akibotHost, akibotPort, obstacleComponent, obstacleDescription);
		
		tankClient.start();
		gyroscopeClient.start();
		// speechSynthesisClient.start();
		distanceMeteriClient.start();
		obstacleClient.start();

		
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
