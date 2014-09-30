package sandbox.messaging.soap.object;

import java.io.*;
import java.net.*;

public class SimpleServer {

	public static void main(String args[]) {
		int port = 2002;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Start");

			Socket socket = serverSocket.accept();

			System.out.println("SERVER: Accepted");

			InputStream inputStream = socket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			Object object;
			
			while (  (object = objectInputStream.readObject()) != null) {
				System.out.println("SERVER: loop start");
				

				if (object instanceof MyObject) {
					MyObject myObject = (MyObject) object;
					System.out.println("SERVER: Received MyObject: " + myObject.getName());
				} 
				else {
					System.out.println("SERVER: Unknown Object!");
					break;
				}
				
				System.out.println("SERVER: loop end");
			}

			inputStream.close();
			socket.close();
			serverSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SEVER: End");

	}

}
