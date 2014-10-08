package com.akibot.kiss.launcher;

import java.io.IOException;

import com.akibot.kiss.component.DistanceMeter;
import com.akibot.kiss.server.Client;
import com.akibot.kiss.server.Server;

public class ServerLauncher {

	public static void main(String[] args) throws Exception {
		// Start Server THREAD:
		Server server = new Server(2002);

		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}
		
	}
}
