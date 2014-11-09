package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.engine.server.Server;
import com.akibot.tanktrack.component.gyroscope.GyroscopeComponent;
import com.akibot.tanktrack.component.gyroscope.GyroscopeRequest;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;

public class AkiBotLauncher {
	static final Logger log = LogManager.getLogger(AkiBotLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		String host = "localhost";
		int port = 2000;
		Server server = new Server(port);
		server.start();

		TankTrackComponent tankTrackComponent = new DD1TankTrackComponent();
		ClientDescription tankTrackDescription = new ClientDescription("akibot.tanktrack");
		tankTrackDescription.getTopicList().add(new StickMotionRequest());
		Client tankClient = new Client(host, port, tankTrackComponent, tankTrackDescription);

		GyroscopeComponent gyroscopeComponent = new HMC5883LGyroscopeComponent(337, -106, 486, 180);
		ClientDescription gyroscopeClientDescription = new ClientDescription("akibot.gyroscope");
		gyroscopeClientDescription.getTopicList().add(new GyroscopeRequest());
		Client gyroscopeClient = new Client(host, port, gyroscopeComponent, gyroscopeClientDescription);

		tankClient.start();
		gyroscopeClient.start();

		// LOOP forever:
		while (true) {
			Thread.sleep(1000);
		}

	}

}
