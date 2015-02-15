package com.akibot.test.messaging;

import com.akibot.engine.server.Server;

public class StartRemoteServerForTests {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting server...");
		Server server;
		server = new Server(2002);
		server.start();

		System.out.println("started.");
		
		while (true) {
			Thread.sleep(10000);
		}
	}
}
