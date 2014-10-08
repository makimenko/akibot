package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.request.AuthorizationRequest;
import com.akibot.kiss.message.response.AuthorizationResponse;
import com.akibot.kiss.message.response.ConnectionAcceptedResponse;

public class ClientAuthorization {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Component component;
	
	public ClientAuthorization(Socket socket, Component component)  throws Exception {
		this.component = component;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}
	
	public void authorize() throws Exception {
		Object obj;
		while (true) {
			obj = in.readObject();
			if (obj instanceof AuthorizationRequest) {
				AuthorizationResponse authorizationResponse = new AuthorizationResponse();
				authorizationResponse.setName("X");
				authorizationResponse.setComponentClassName(component.getClass().toString());
				out.writeObject(authorizationResponse);
			} else if (obj instanceof ConnectionAcceptedResponse) {
				break;
			} else {
				throw new Exception("Authorization failed");
			}
		}
	}
	


	
}
