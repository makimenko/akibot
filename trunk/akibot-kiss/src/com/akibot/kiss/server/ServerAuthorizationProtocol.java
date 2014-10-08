package com.akibot.kiss.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.request.AuthorizationRequest;
import com.akibot.kiss.message.response.AuthorizationResponse;
import com.akibot.kiss.message.response.ConnectionAcceptedResponse;
import com.akibot.kiss.types.SimpleProtocolPhaseType;

public class ServerAuthorizationProtocol {
	private SimpleProtocolPhaseType phase;
	private ClientDescription clientDescription;
	private ObjectOutputStream out;
	private ObjectInputStream in;
		
	public ServerAuthorizationProtocol(Socket socket) throws IOException {
		phase = SimpleProtocolPhaseType.START;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}
	
	public void authorize() throws Exception {
		out.writeObject(processInput(null));
		
		while (true) {
			Object obj = in.readObject();
			out.writeObject(processInput(obj));
			
			if (getPhase()==SimpleProtocolPhaseType.SUCCEDED || getPhase()==SimpleProtocolPhaseType.FAILED) {
				break;
			}								
		}
		
	}
	
	
	public Message processInput(Object object) throws Exception {

		if (phase == SimpleProtocolPhaseType.START) {
			phase = SimpleProtocolPhaseType.INFO_REQUESTED;
			return new AuthorizationRequest();
		} else if (phase == SimpleProtocolPhaseType.INFO_REQUESTED && object instanceof AuthorizationResponse) {
			AuthorizationResponse authorizationResponse = (AuthorizationResponse)object;
			// TODO: add validation
			clientDescription = new ClientDescription(authorizationResponse.getName(), authorizationResponse.getComponentClassName());
			phase = SimpleProtocolPhaseType.SUCCEDED;
			return new ConnectionAcceptedResponse();
		} else {
			phase = SimpleProtocolPhaseType.FAILED;
			return null;
		}

	}

	public SimpleProtocolPhaseType getPhase() {
		return phase;
	}

	public ClientDescription getClientDescription() {
		return clientDescription;
	}
	
}
