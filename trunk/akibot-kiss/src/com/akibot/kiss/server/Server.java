package com.akibot.kiss.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.message.Message;
import com.akibot.kiss.types.SimpleProtocolPhaseType;

public class Server {
	static final Logger log = LogManager.getLogger(Server.class.getName());
	private ConcurrentHashMap<ClientDescription, Connection> clientList;
	private LinkedBlockingQueue<Object> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		log.info("Server starting...");
		clientList = new ConcurrentHashMap<ClientDescription, Connection>();
		messages = new LinkedBlockingQueue<Object>();
		serverSocket = new ServerSocket(port);
		log.info("Server started");

		Thread accept = new Thread() {
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						log.debug("New connection accepted");

						ServerAuthorizationProtocol protocol = new ServerAuthorizationProtocol(socket);

						protocol.authorize();

						log.debug("Protocol phase = " + protocol.getPhase());

						if (protocol.getPhase() == SimpleProtocolPhaseType.SUCCEDED) {
							ClientDescription clientDescription = protocol.getClientDescription();
							Connection newConnection = new Connection(socket, messages);
							clientList.putIfAbsent(clientDescription, newConnection);
						} else {
							socket.close();
						}

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		};

		accept.setDaemon(true);
		accept.start();

		ServerMessageHandler serverMessageHandler = new ServerMessageHandler(this, messages);
		serverMessageHandler.setDaemon(true);
		serverMessageHandler.start();
	}

	public void sendToOne(int index, Object message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void broadcast(Object message) {
		for (ConcurrentHashMap.Entry<ClientDescription, Connection> entry : clientList.entrySet()) {
			ClientDescription clientDescription = entry.getKey();

			ArrayList<Message> topicList = clientDescription.getTopicList();

			Iterator<Message> i = topicList.iterator();
			while (i.hasNext()) {
				Message topicMessage = (Message) i.next();
				if (message.getClass().getName().equals(topicMessage.getClass().getName())) {
					entry.getValue().write(message);

				}

			}

			// if
			// (clientDescription.getName().equalsIgnoreCase("Distance Meter"))
			// {
			// }
		}
	}

}
