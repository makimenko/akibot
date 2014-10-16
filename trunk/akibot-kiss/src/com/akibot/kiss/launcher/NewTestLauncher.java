package com.akibot.kiss.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.awtcontroller.AwtControllerComponent;
import com.akibot.kiss.component.distance.DistanceMeterComponent;
import com.akibot.kiss.message.Response;
import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.ClientDescription;
import com.akibot.kiss.server.Server;

public class NewTestLauncher {
	static final Logger log = LogManager.getLogger(NewTestLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		String host = "localhost";
		int port = 2000;
		Server server = new Server(port);

		// Start Client THREAD:
		DistanceMeterComponent distanceMeterComponent = new DistanceMeterComponent();
		ClientDescription distanceMeterDescription = new ClientDescription("Distance Meter");
		distanceMeterDescription.getTopicList().add(new DistanceRequest());
		Client distanceClient = new Client(host, port, distanceMeterComponent, distanceMeterDescription);
		distanceClient.start();

		AwtControllerComponent awtControllerComponent = new AwtControllerComponent();
		ClientDescription awtControllerDescription = new ClientDescription("Awt Controller");
		awtControllerDescription.getTopicList().add(new Response());
		Client awtControllerClient = new Client(host, port, awtControllerComponent, awtControllerDescription);
		awtControllerClient.start();

		// LOOP forever:
		while (true) {
			Thread.sleep(1000);
		}

	}

}
