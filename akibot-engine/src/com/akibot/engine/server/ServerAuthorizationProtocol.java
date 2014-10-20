package com.akibot.engine.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.akibot.engine.message.Message;
import com.akibot.engine.message.request.AuthorizationRequest;
import com.akibot.engine.message.response.AuthorizationResponse;
import com.akibot.engine.message.response.ConnectionAcceptedResponse;
import com.akibot.engine.types.SimpleAuthorizationProtocolPhaseType;

public class ServerAuthorizationProtocol {
	private ClientDescription clientDescription;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private SimpleAuthorizationProtocolPhaseType phase;

	public ServerAuthorizationProtocol(Socket socket) throws IOException {
		phase = SimpleAuthorizationProtocolPhaseType.START;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void authorize() throws Exception {
		out.writeObject(processInput(null));

		while (true) {
			Object obj = in.readObject();
			out.writeObject(processInput(obj));

			if (getPhase() == SimpleAuthorizationProtocolPhaseType.SUCCEDED || getPhase() == SimpleAuthorizationProtocolPhaseType.FAILED) {
				break;
			}
		}

	}

	public ClientDescription getClientDescription() {
		return clientDescription;
	}

	public SimpleAuthorizationProtocolPhaseType getPhase() {
		return phase;
	}

	public Message processInput(Object object) throws Exception {

		if (phase == SimpleAuthorizationProtocolPhaseType.START) {
			phase = SimpleAuthorizationProtocolPhaseType.INFO_REQUESTED;
			return new AuthorizationRequest();
		} else if (phase == SimpleAuthorizationProtocolPhaseType.INFO_REQUESTED && object instanceof AuthorizationResponse) {
			AuthorizationResponse authorizationResponse = (AuthorizationResponse) object;
			// TODO: add validation

			clientDescription = authorizationResponse.getClientDescription();
			phase = SimpleAuthorizationProtocolPhaseType.SUCCEDED;
			return new ConnectionAcceptedResponse();
		} else {
			phase = SimpleAuthorizationProtocolPhaseType.FAILED;
			return null;
		}

	}

}
