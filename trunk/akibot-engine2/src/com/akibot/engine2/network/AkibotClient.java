package com.akibot.engine2.network;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.Component;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;

public class AkibotClient extends Thread {
	private static final Logger log = LogManager.getLogger(AkibotClient.class.getName());
	private List<ClientDescription> clientDescriptionList;

	private Component component;
	private IncommingMessageManager incommingMessageManager;
	private ClientDescription myClientDescription;

	private InetSocketAddress myInetSocketAddress;
	private OutgoingMessageManager outgoingMessageManager;
	private InetSocketAddress parentSocketAddress;
	private DatagramSocket socket;
	private SynchronizedMessageManager synchronizedMessageManager;

	public AkibotClient(Component component, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this(component, null, parentSocketAddress);
	}

	public AkibotClient(Component component, int port) throws SocketException, UnknownHostException {
		this(component, port, null);
	}

	public AkibotClient(Component component, Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		log.debug("Initializing...");
		this.setDaemon(true);
		this.component = component;
		this.socket = (port == null ? new DatagramSocket() : new DatagramSocket(port));
		// this.socket.setTrafficClass(0x04);
		this.myInetSocketAddress = new InetSocketAddress(socket.getLocalAddress().getLocalHost(), socket.getLocalPort());
		this.parentSocketAddress = parentSocketAddress;
		this.incommingMessageManager = new IncommingMessageManager(this);
		this.outgoingMessageManager = new OutgoingMessageManager(this);
		this.synchronizedMessageManager = new SynchronizedMessageManager(this);
		this.clientDescriptionList = new ArrayList<ClientDescription>();
		this.component.setAkibotClient(this);

		myClientDescription = new ClientDescription(component.getName(), getMyInetSocketAddress());

		if (getParentSocketAddress() != null) {
			ClientDescription parentClientDescription = new ClientDescription(null, getParentSocketAddress());
			clientDescriptionList.add(parentClientDescription);
		}

		log.info(component + ": initialized.");
	}

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public Component getComponent() {
		return component;
	}

	public IncommingMessageManager getIncommingMessageManager() {
		return incommingMessageManager;
	}

	public ClientDescription getMyClientDescription() {
		return myClientDescription;
	}

	public InetSocketAddress getMyInetSocketAddress() {
		return myInetSocketAddress;
	}

	public OutgoingMessageManager getOutgoingMessageManager() {
		return outgoingMessageManager;
	}

	public InetSocketAddress getParentSocketAddress() {
		return parentSocketAddress;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public SynchronizedMessageManager getSynchronizedMessageManager() {
		return synchronizedMessageManager;
	}

	public void onSystemMessageReceived(Message message) throws Exception {
		log.trace(this + ": onSystemMessageReceived (from=" + message.getFrom() + "): " + message);
		if (message instanceof ClientDescriptionRequest) {
			ClientDescriptionRequest request = (ClientDescriptionRequest) message;
			clientDescriptionList = ClientDescriptionUtils.merge(request.getClientDescription(), clientDescriptionList);
			ClientDescriptionResponse response = new ClientDescriptionResponse();
			response.setClientDescriptionList(clientDescriptionList);
			outgoingMessageManager.broadcastMessage(response);
		} else if (message instanceof ClientDescriptionResponse) {
			ClientDescriptionResponse response = (ClientDescriptionResponse) message;
			clientDescriptionList = ClientDescriptionUtils.merge(myClientDescription, response.getClientDescriptionList(), clientDescriptionList);
			// broadcastMessage(response);
		}
	}

	public String printClients() {
		StringBuffer sb = new StringBuffer();
		for (ClientDescription client : getClientDescriptionList()) {
			sb.append(client);
			sb.append("\n");
		}
		return sb.toString();
	}

	public void refreshClientDescriptionList() {
		log.trace(this + ": refreshClientDescriptionList: " + myClientDescription);
		try {
			ClientDescriptionRequest clientDescriptionRequest = new ClientDescriptionRequest();
			clientDescriptionRequest.setClientDescription(myClientDescription);
			outgoingMessageManager.broadcastMessage(clientDescriptionRequest);
		} catch (FailedToSendMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setClientDescriptionList(List<ClientDescription> clientDescriptionList) {
		this.clientDescriptionList = clientDescriptionList;
	}

	public void setIncommingMessageManager(IncommingMessageManager incommingMessageManager) {
		this.incommingMessageManager = incommingMessageManager;
	}

	public void setMyClientDescription(ClientDescription myClientDescription) {
		this.myClientDescription = myClientDescription;
	}

	public void setOutgoingMessageManager(OutgoingMessageManager outgoingMessageManager) {
		this.outgoingMessageManager = outgoingMessageManager;
	}

	public void setSynchronizedMessageManager(SynchronizedMessageManager synchronizedMessageManager) {
		this.synchronizedMessageManager = synchronizedMessageManager;
	}

	public void start() {
		log.debug(component + ": Starting AkibotClient...");
		super.start();
		component.start();
		incommingMessageManager.start();
		outgoingMessageManager.start();
		synchronizedMessageManager.start();
		refreshClientDescriptionList();
		log.debug(component + ": started.");
	}

	public String toString() {
		if (component != null) {
			return component.getName();
		} else {
			return super.toString();
		}
	}

}
