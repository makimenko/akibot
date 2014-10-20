package com.akibot.engine.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.engine.message.request.AuthorizationRequest;
import com.akibot.engine.message.response.AuthorizationResponse;
import com.akibot.engine.message.response.ConnectionAcceptedResponse;

public class ClientAuthorization {
	private Client client;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ClientAuthorization(Socket socket, Client client) throws Exception {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		this.client = client;
	}

	public void authorize() throws Exception {
		Object obj;
		while (true) {
			obj = in.readObject();
			if (obj instanceof AuthorizationRequest) {
				AuthorizationResponse authorizationResponse = new AuthorizationResponse();
				authorizationResponse.setClientDescription(client.getClientDescription());
				out.writeObject(authorizationResponse);
			} else if (obj instanceof ConnectionAcceptedResponse) {
				break;
			} else {
				throw new Exception("Authorization failed");
			}
		}
	}

}
