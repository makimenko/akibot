package com.akibot.kiss.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	private int port;
	private ServerSocket serverSocket;

	public Server(int port) {
		log.info("Server starting (" + port + ")...");
		this.clientList = new ConcurrentHashMap<ClientDescription, Connection>();
		this.messages = new LinkedBlockingQueue<Object>();
		this.port = port;
	}

	public void broadcast(Message message) {
		for (ConcurrentHashMap.Entry<ClientDescription, Connection> entry : clientList.entrySet()) {
			ClientDescription clientDescription = entry.getKey();
			Connection connection = entry.getValue();
			String to = message.getTo();
			boolean isIterested = clientDescription.isInterestedInMessage(message);
			if (to == null && isIterested) {
				connection.write(message);
			} else if (to != null && clientDescription.getName().matches(to) && isIterested) {
				connection.write(message);
			}
		}
	}

	public void start() throws IOException {
		this.serverSocket = new ServerSocket(port);
		log.info("Server started");

		Thread accept = new Thread() {
			@Override
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
							Connection newConnection = new Connection(socket, messages, null);
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

}
