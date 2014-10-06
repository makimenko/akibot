package com.akibot.kiss.launcher;

import com.akibot.kiss.component.DistanceMeter;
import com.akibot.kiss.server.Client;

public class DistanceMeterLauncher {

	public static void main(String args[]) throws Exception {
		DistanceMeter distanceMeter = new DistanceMeter();

		Client client = new Client("localhost", 2002, distanceMeter);
		client.send("test");
		int i = 0;

		while (true) {
			client.send("hello from client");

			i++;
			if (i == 5) {
				i = 0;
				client.send("X");
			}
			Thread.sleep(1000);
		}

	}
}
