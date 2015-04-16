package sandbox.messaging.soap.recommended;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
	private ArrayList<ConnectionToClient> clientList;
	private LinkedBlockingQueue<Object> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		clientList = new ArrayList<ConnectionToClient>();
		messages = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);

		Thread accept = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket s = serverSocket.accept();
						clientList.add(new ConnectionToClient(s));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		accept.setDaemon(true);
		accept.start();

		Thread messageHandling = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Object message = messages.take();

						if (((String) message).equalsIgnoreCase("X")) {
							System.out.println("Broadcasting: " + message);
							sendToAll("HEY!!! broadcasting X!");
						}
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

	private class ConnectionToClient {
		ObjectInputStream in;
		ObjectOutputStream out;

		ConnectionToClient(Socket socket) throws IOException {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			Thread read = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							Object obj = in.readObject();
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

			read.setDaemon(true); // terminate when main ends
			read.start();
		}

		public void write(Object obj) {
			try {
				out.writeObject(obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll(Object message) {
		for (ConnectionToClient client : clientList)
			client.write(message);
	}

}
