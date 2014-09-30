package sandbox.messaging.soap.object;

import java.net.*;
import java.io.*;

public class SimpleClient {

	public static void main(String args[]) {
		int port = 2002;
		try {
			System.out.println("CLIENT: Start");
			Socket socket = new Socket("localhost", port);
			Thread.sleep(1000);

			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

			for (int i = 0; i <= 1; i++) {
				MyObject myObject = new MyObject();
				myObject.setName("Nick");
				Thread.sleep(500);
				System.out.println("CLIENT: Sending...");
				objectOutputStream.writeObject(myObject);
				System.out.println("CLIENT: sent.");
			}

			objectOutputStream.close();
			outputStream.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("CLIENT: " + e);
		}
		System.out.println("CLIENT: End");

	}
}
