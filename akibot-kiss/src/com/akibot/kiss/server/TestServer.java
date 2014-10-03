package com.akibot.kiss.server;

import java.io.IOException;
import java.util.logging.Logger;

public class TestServer {

	static Logger log = Logger.getLogger(TestServer.class.getName());

	  
	public static void main(String[] args) throws IOException, InterruptedException {
		log.info("Started");
		System.out.println("TestServer: start");
		Server server = new Server(2002);
		System.out.println("TestServer: listening...");
		while (true) {
			Thread.sleep(10000);
		}
	}
}
