package sandbox.messaging.soap.object;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.ws.Response;

public class SimpleServer {

	public static void main(String args[]) {
		int port = 2002;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Start");

			Socket socket = serverSocket.accept();

			System.out.println("SERVER: Accepted");

			
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					

			Object object;

			while ((object = objectInputStream.readObject()) != null) {
				//System.out.println("SERVER: loop start");

				if (object instanceof MyObject) {
					MyObject myObject = (MyObject) object;
					//System.out.println("SERVER: Received MyObject: " + myObject.getName());
					MyObject response = new MyObject();
					response.setName("RESP: "+myObject.getName());
					objectOutputStream.writeObject(response);		
				} else {
					System.out.println("SERVER: Unknown Object!");
					break;
				}
				
				//System.out.println("SERVER: loop end");
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
