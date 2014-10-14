package sandbox.messaging.soap.recommended;

import java.io.IOException;

public class TestClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("TestClent: start");
		Client client = new Client("localhost", 2002);
		System.out.println("TestClent: sending...");
		client.send("test");
		System.out.println("TestClent: end");
		int i = 0;

		while (true) {
			// server.sendToOne(1, "test");
			client.send("hello from client");

			i++;
			if (i == 10) {
				client.send("X");
			}
			Thread.sleep(1000);
		}

	}

}
