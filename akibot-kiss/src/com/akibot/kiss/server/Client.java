package com.akibot.kiss.server;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.kiss.component.Component;
import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.Request;
import com.akibot.kiss.message.Response;

public class Client {
	static final Logger log = LogManager.getLogger(Client.class.getName());

	private Connection server;
	private Component component;
	private LinkedBlockingQueue<Object> messages;
	private Socket socket;
	private ClientDescription clientDescription;
	private String syncId;
	private Response syncResponse;

	public Client(String IPAddress, int port, Component component, ClientDescription clientDescription) throws Exception {
		log.info("Connecting to server...");
		this.setClientDescription(clientDescription);
		this.socket = new Socket(IPAddress, port);
		this.messages = new LinkedBlockingQueue<Object>();
		this.component = component;
	}

	public void start() throws Exception {
		ClientAuthorization clientAuthorization = new ClientAuthorization(socket, this);
		clientAuthorization.authorize();

		server = new Connection(socket, messages, this);
		log.info("Connected to server");

		component.setClient(this);
		component.start();
		ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this, messages, component);
		clientMessageHandler.setDaemon(true);
		clientMessageHandler.start();
	}

	public void send(Message msg) {
		msg.setFrom(clientDescription.getName());
		server.write(msg);
	}

	public ClientDescription getClientDescription() {
		return clientDescription;
	}

	public void setClientDescription(ClientDescription clientDescription) {
		this.clientDescription = clientDescription;
	}

	public Response syncRequest(Request request, int timeout) throws InterruptedException, CloneNotSupportedException {
		Request newRequest = (Request) request.clone();
		syncResponse = null;

		syncId = UUID.randomUUID().toString();
		newRequest.setFrom(clientDescription.getName());
		newRequest.setSyncId(syncId);

		log.debug("Sync messasge sent: " + newRequest.getSyncId() + ": " + newRequest);
		server.write(newRequest);

		synchronized (this.syncId) {
			this.syncId.wait(timeout);
		}

		if (syncResponse == null) {
			throw new InterruptedException("Timeout occured while waiting sync response");
		} else {
			log.debug("Sync messasge received: " + syncResponse.getSyncId() + ": " + syncResponse);
		}
		return syncResponse;
	}

	public Response getSyncResponse() {
		return syncResponse;
	}

	public void setSyncResponse(Response syncResponse) {
		this.syncResponse = syncResponse;
	}

	public String getSyncId() {
		return syncId;
	}

}
