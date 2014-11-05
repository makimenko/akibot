package com.akibot.tanktrack.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.message.Response;
import com.akibot.engine.server.Client;
import com.akibot.engine.server.ClientDescription;
import com.akibot.tanktrack.component.awtcontroller.AwtControllerComponent;

public class AwtControllerLauncher {
	static final Logger log = LogManager.getLogger(AwtControllerLauncher.class.getName());

	public static void main(String[] args) throws Exception {

		String host = "192.168.0.103";
		int port = 2000;

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
