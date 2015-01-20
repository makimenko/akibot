package sandbox.messaging.soap.simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class StringServer {

	public static void main(String args[]) {
		int port = 2002;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Start");

			Socket socket = serverSocket.accept();

			System.out.println("SERVER: Accepted");

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			System.out.println("Listening...");
			String txt;
			while ((txt = in.readLine()) != null) {
				out.println("RESP: " + txt);
			}

			in.close();
			out.close();
			socket.close();
			serverSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SEVER: End");

	}

}
