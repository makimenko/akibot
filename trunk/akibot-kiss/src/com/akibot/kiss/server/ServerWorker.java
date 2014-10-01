package com.akibot.kiss.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;


public class ServerWorker implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;

	public ServerWorker(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
		
			InputStream inputStream = clientSocket.getInputStream();
			OutputStream outputStream = clientSocket.getOutputStream();
			
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			Object object;
			
			while (  (object = objectInputStream.readObject()) != null) {
				System.out.println("SERVER: loop start");
				

				if (object instanceof CommandMessage) {
					CommandMessage commandMessage = (CommandMessage) object;
					System.out.println("SERVER: Command Received: "+commandMessage.getCommand());
				} else if (object instanceof DistanceStatusMessage) {
					DistanceStatusMessage distanceStatusMessage = (DistanceStatusMessage) object;
					System.out.println("SERVER: Status Received: "+distanceStatusMessage.getMeters());
				} else {
					System.out.println("SERVER: Unknown Object!");
					break;
				}
				
				System.out.println("SERVER: loop end");
			}

			inputStream.close();
			outputStream.close();
			
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}