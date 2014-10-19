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

	private ClientDescription clientDescription;
	private Component component;
	private LinkedBlockingQueue<Object> messages;
	private Connection server;
	private Socket socket;
	private String syncId;
	private Response syncResponse;

	public Client(String IPAddress, int port, Component component, ClientDescription clientDescription) throws Exception {
		log.info(clientDescription.getName() + " - Connecting to server (" + IPAddress + ":" + port + ")...");
		this.setClientDescription(clientDescription);
		this.socket = new Socket(IPAddress, port);
		this.messages = new LinkedBlockingQueue<Object>();
		this.component = component;
	}

	public ClientDescription getClientDescription() {
		return clientDescription;
	}

	public String getSyncId() {
		return syncId;
	}

	public Response getSyncResponse() {
		return syncResponse;
	}

	public void send(Message msg) {
		msg.setFrom(clientDescription.getName());
		server.write(msg);
	}

	public void setClientDescription(ClientDescription clientDescription) {
		this.clientDescription = clientDescription;
	}

	public void setSyncResponse(Response syncResponse) {
		this.syncResponse = syncResponse;
	}

	public void start() throws Exception {
		ClientAuthorization clientAuthorization = new ClientAuthorization(socket, this);
		clientAuthorization.authorize();

		server = new Connection(socket, messages, this);
		log.info(clientDescription.getName() + " - Connected to server.");

		component.setClient(this);
		component.run();
		ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this, messages, component);
		clientMessageHandler.setDaemon(true);
		clientMessageHandler.start();
	}

	public Response syncRequest(Request request, int timeout) throws InterruptedException, CloneNotSupportedException {
		Request newRequest = (Request) request.clone();
		syncResponse = null;

		syncId = UUID.randomUUID().toString();
		newRequest.setFrom(clientDescription.getName());
		newRequest.setSyncId(syncId);

		log.trace("Sync messasge sent: " + newRequest + " (syncId=" + newRequest.getSyncId() + ")");
		server.write(newRequest);

		synchronized (this.syncId) {
			this.syncId.wait(timeout);
		}

		if (syncResponse == null) {
			throw new InterruptedException("Timeout occured while waiting sync response");
		} else {
			log.trace("Sync messasge received: " + syncResponse.getSyncId() + ": " + syncResponse);
		}
		return syncResponse;
	}

}
