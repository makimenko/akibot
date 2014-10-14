package com.akibot.kiss.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.DistanceMeter;
import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.Server;

public class TestLauncher {
	static final Logger log = LogManager.getLogger(TestLauncher.class.getName());

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		int port = 2000;
		Server server = new Server(port);

		// Start Client THREAD:
		DistanceMeter distanceMeter = new DistanceMeter();
		Client client = new Client("localhost", port, distanceMeter);

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
