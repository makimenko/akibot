package com.akibot.kiss.server;

import java.io.IOException;

public class TestClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		Client client = new Client("localhost", 2002);
		client.send("test");
		int i = 0;

		while (true) {
			client.send("hello from client");

			i++;
			if (i == 10) {
				i=0;
				client.send("X");				
			}
			Thread.sleep(1000);
		}

	}

}
