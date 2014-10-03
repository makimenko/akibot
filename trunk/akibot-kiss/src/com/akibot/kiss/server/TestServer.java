package com.akibot.kiss.server;

import java.io.IOException;

public class TestServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("TestServer: start");
		Server server = new Server(2002);
		System.out.println("TestServer: listening...");
		while (true) {
			Thread.sleep(2000);
		}
	}
}
