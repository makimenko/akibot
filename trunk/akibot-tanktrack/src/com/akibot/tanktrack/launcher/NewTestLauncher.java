package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.message.Response;
import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.engine.server.Server;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;

public class NewTestLauncher {
	static final Logger log = LogManager.getLogger(NewTestLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		String host = "localhost";
		int port = 2000;
		Server server = new Server(port);
		server.start();

		// Start Client THREADs:
		DistanceMeterComponent distanceRightComponent = new DistanceMeterComponent();
		ClientDescription distanceRightDescription = new ClientDescription("akibot.distance.right");
		distanceRightDescription.getTopicList().add(new DistanceRequest());
		Client distanceRightClient = new Client(host, port, distanceRightComponent, distanceRightDescription);

		DistanceMeterComponent distanceLeftComponent = new DistanceMeterComponent();
		ClientDescription distanceLeftDescription = new ClientDescription("akibot.distance.left");
		distanceLeftDescription.getTopicList().add(new DistanceRequest());
		Client distanceLeftClient = new Client(host, port, distanceLeftComponent, distanceLeftDescription);

		TankTrackComponent tankTrackComponent = new TankTrackComponent();
		ClientDescription tankTrackDescription = new ClientDescription("akibot.tanktrack");
		tankTrackDescription.getTopicList().add(new StickMotionRequest());
		Client tankClient = new Client(host, port, tankTrackComponent, tankTrackDescription);

		AwtControllerComponent awtControllerComponent = new AwtControllerComponent();
		ClientDescription awtControllerDescription = new ClientDescription("Awt Controller");
		awtControllerDescription.getTopicList().add(new Response());
		Client awtControllerClient = new Client(host, port, awtControllerComponent, awtControllerDescription);

		distanceRightClient.start();
		distanceLeftClient.start();
		tankClient.start();
		awtControllerClient.start();

		// LOOP forever:
		while (true) {
			Thread.sleep(1000);
		}

	}

}
