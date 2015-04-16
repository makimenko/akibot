package sandbox.messaging.soap.recommended;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
	private ConnectionToServer server;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;

	public Client(String IPAddress, int port) throws IOException {
		socket = new Socket(IPAddress, port);
		messages = new LinkedBlockingQueue<Object>();
		server = new ConnectionToServer(socket);

		Thread messageHandling = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Object message = messages.take();
						// Do some handling here...
						System.out.println("Message Received: " + message);
					} catch (InterruptedException e) {
					}
				}
			}
		};

		messageHandling.setDaemon(true);
		messageHandling.start();
	}

	private class ConnectionToServer {
		ObjectInputStream in;
		ObjectOutputStream out;

		ConnectionToServer(Socket socket) throws IOException {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			Thread read = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							Object obj = in.readObject();
							System.out.println("Client.readObject");
							messages.put(obj);
						} catch (IOException e) {
							e.printStackTrace();
							break;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							break;
						}
					}
				}
			};

			read.setDaemon(true);
			read.start();
		}

		private void write(Object obj) {
			try {
				out.writeObject(obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void send(Object obj) {
		server.write(obj);
	}
}
