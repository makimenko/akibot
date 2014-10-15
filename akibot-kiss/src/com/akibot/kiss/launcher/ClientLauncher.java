package com.akibot.kiss.launcher;

import com.akibot.kiss.component.distance.DistanceMeterComponent;
import com.akibot.kiss.server.Client;

public class ClientLauncher {

	public static void main(String[] args) throws Exception {

		// Start Client THREAD:
		DistanceMeterComponent distanceMeter = new DistanceMeterComponent();
		Client client = new Client("localhost", 2002, distanceMeter);

		// LOOP forever:
		int i = 0;
		while (true) {
			client.send("hello from client");

			if (i++ == 5) {
				i = 0;
				client.send("X");
			}
			Thread.sleep(1000);
		}

	}
}
