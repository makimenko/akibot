package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.engine.server.Server;
import com.akibot.tanktrack.component.gyroscope.GyroscopeComponent;
import com.akibot.tanktrack.component.gyroscope.GyroscopeRequest;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;

public class AkiBotLauncher {
	static final Logger log = LogManager.getLogger(AkiBotLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		String akibotHost = "localhost";
		int akibotPort = 2000;

		String maryttsHost = "192.168.0.100";
		int maryttsPort = 59125;
		String maryttsVoice = "cmu-slt-hsmm";

		// Server:
		Server server = new Server(akibotPort);
		server.start();

		// TankTrack:
		TankTrackComponent tankTrackComponent = new DD1TankTrackComponent();
		ClientDescription tankTrackDescription = new ClientDescription("akibot.tanktrack");
		tankTrackDescription.getTopicList().add(new StickMotionRequest());
		Client tankClient = new Client(akibotHost, akibotPort, tankTrackComponent, tankTrackDescription);

		// Gyroscope:
		GyroscopeComponent gyroscopeComponent = new HMC5883LGyroscopeComponent(337, -106, 486, 180);
		ClientDescription gyroscopeClientDescription = new ClientDescription("akibot.gyroscope");
		gyroscopeClientDescription.getTopicList().add(new GyroscopeRequest());
		Client gyroscopeClient = new Client(akibotHost, akibotPort, gyroscopeComponent, gyroscopeClientDescription);

		// SpeechSynthesis:
		SpeechSynthesisComponent speechSynthesisComponent = new SpeechSynthesisComponent(maryttsHost, maryttsPort, maryttsVoice);
		ClientDescription speechSynthesisDescription = new ClientDescription("akibot.speech.synthesis");
		speechSynthesisDescription.getTopicList().add(new SpeechSynthesisRequest());
		Client speechSynthesisClient = new Client(akibotHost, akibotPort, speechSynthesisComponent, speechSynthesisDescription);

		tankClient.start();
		gyroscopeClient.start();
		speechSynthesisClient.start();

		// LOOP forever:
		while (true) {
			Thread.sleep(1000);
		}

	}

}
