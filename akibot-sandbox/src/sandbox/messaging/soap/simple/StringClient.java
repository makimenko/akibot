package sandbox.messaging.soap.simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StringClient {

	public static void main(String args[]) {
		int port = 2002;
		String host = "raspberrypi";
		try {
			System.out.println("CLIENT: Start");
			Socket socket = new Socket(host, port);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			System.out.println("Listening...");
			long timeout = 10000;
			long startTime = System.currentTimeMillis();
			int count = 0;

			while (System.currentTimeMillis() - startTime < timeout) {
				count++;
				out.println("Michael " + count);
				String response = in.readLine();
			}
			long duration = System.currentTimeMillis() - startTime;

			System.out.println("Performance Stats: count=" + count + ", duration=" + duration + ", avg=" + (duration / count));
			// Performance Stats: count=2020, duration=10019, avg=4

			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("CLIENT: " + e);
		}
		System.out.println("CLIENT: End");

	}
}
