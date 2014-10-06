package com.akibot.kiss.server;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TestServer {
	static final Logger log = LogManager.getLogger(TestServer.class.getName());
	  
	public static void main(String[] args) throws IOException, InterruptedException {
		Server server = new Server(2002);
		while (true) {
			Thread.sleep(10000);
		}
	}
}
