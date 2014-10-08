package com.akibot.kiss.launcher;

import com.akibot.kiss.component.DistanceMeter;
import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.Server;

public class TestLauncher {

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		Server server = new Server(2002);

		// Start Client THREAD:
		DistanceMeter distanceMeter = new DistanceMeter();
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
