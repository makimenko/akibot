package com.akibot.kiss.launcher;

import com.akibot.kiss.server.Server;

public class ServerLauncher {

	public static void main(String[] args) throws Exception {
		Server server = new Server(2002);
		while (true) {
			Thread.sleep(10000);
		}
	}
	
}
