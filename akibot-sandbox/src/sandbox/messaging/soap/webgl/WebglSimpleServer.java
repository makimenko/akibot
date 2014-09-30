package sandbox.messaging.soap.webgl;

import java.io.*;
import java.net.*;

public class WebglSimpleServer {

	public static void main(String args[]) {
		int port = 2002;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Start");

			Socket socket = serverSocket.accept();

			System.out.println("SERVER: Accepted");

			InputStream inputStream = socket.getInputStream();
			
			BufferedReader in =new BufferedReader(new InputStreamReader(inputStream));
			
			
			String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("SERVER RECEIVED: "+inputLine);
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
