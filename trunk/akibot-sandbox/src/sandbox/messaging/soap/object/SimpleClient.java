package sandbox.messaging.soap.object;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleClient {

	public static void main(String args[]) {
		int port = 2002;
		String host = "raspberrypi";
		try {
			System.out.println("CLIENT: Start");
			Socket socket = new Socket(host, port);

			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

			long timeout = 10000;
			long startTime = System.currentTimeMillis();
			int count = 0;
			while (System.currentTimeMillis() - startTime < timeout) {
				count++;
				MyObject myObject = new MyObject();
				myObject.setName("Nick");
				objectOutputStream.writeObject(myObject);

				MyObject response = (MyObject) objectInputStream.readObject();
				String responseName = response.getName();
			}
			long duration = System.currentTimeMillis() - startTime;

			System.out.println("Performance Stats: count=" + count + ", duration=" + duration + ", avg=" + (duration / count));
			// Performance Stats: count=40, duration=10190, avg=254

			objectOutputStream.close();
			outputStream.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("CLIENT: " + e);
		}
		System.out.println("CLIENT: End");

	}
}
