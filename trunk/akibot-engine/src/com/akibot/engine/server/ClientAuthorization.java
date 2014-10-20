package com.akibot.engine.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.engine.exception.AuthorizationFailedException;
import com.akibot.engine.message.request.AuthorizationRequest;
import com.akibot.engine.message.response.AuthorizationResponse;
import com.akibot.engine.message.response.ConnectionAcceptedResponse;

public class ClientAuthorization {
	private Client client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;

	public ClientAuthorization(Socket socket, Client client) throws IOException {
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		this.client = client;
	}

	public void authorize() throws AuthorizationFailedException {
		while (true) {
			try {
				socket.setSoTimeout(5000);
				Object obj = in.readObject();
				if (obj instanceof AuthorizationRequest) {
					AuthorizationResponse authorizationResponse = new AuthorizationResponse();
					authorizationResponse.setClientDescription(client.getClientDescription());
					out.writeObject(authorizationResponse);
				} else if (obj instanceof ConnectionAcceptedResponse) {
					socket.setSoTimeout(0);
					break;
				} else {
					throw new AuthorizationFailedException();
				}
			} catch (Exception e) {
				throw new AuthorizationFailedException();
			}
		}
	}

}
