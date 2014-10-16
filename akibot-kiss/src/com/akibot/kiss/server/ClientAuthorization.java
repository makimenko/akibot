package com.akibot.kiss.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.kiss.message.request.AuthorizationRequest;
import com.akibot.kiss.message.response.AuthorizationResponse;
import com.akibot.kiss.message.response.ConnectionAcceptedResponse;

public class ClientAuthorization {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Client client;

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
