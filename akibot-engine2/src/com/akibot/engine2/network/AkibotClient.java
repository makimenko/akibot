package com.akibot.engine2.network;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.component.Component;
import com.akibot.engine2.component.configuration.GetConfigurationRequest;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedClientConstructorException;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.monitoring.StatusRequest;
import com.akibot.engine2.monitoring.StatusResponse;

public class AkibotClient extends Thread {
	private static final AkiLogger log = AkiLogger.create(AkibotClient.class);
	private final long startupTime = System.currentTimeMillis();

	private List<ClientDescription> clientDescriptionList;
	private Component component;
	private IncommingMessageManager incommingMessageManager;
	private ClientDescription myClientDescription;
	private ClientDescription dnsClientDescription;
	private InetSocketAddress myInetSocketAddress;
	private OutgoingMessageManager outgoingMessageManager;
	private InetSocketAddress parentSocketAddress;
	private DatagramSocket socket;
	private SynchronizedMessageManager synchronizedMessageManager;
	private boolean executedOnConfigurationClientAvailable = false;

	/**
	 * The network client which has component (Actor Type and Name), receive and send messages via queue.
	 * 
	 * @param name
	 *            Unique Component/Actor Name
	 * @param component
	 *            Actor Type / Component
	 * @param parentSocketAddress
	 *            DNS or Parent Component Address
	 * @throws Exception
	 */
	public AkibotClient(String name, Component component, InetSocketAddress parentSocketAddress) throws FailedClientConstructorException {
		this(name, component, null, parentSocketAddress);
	}

	public AkibotClient(String name, Component component, int port) throws FailedClientConstructorException {
		this(name, component, port, null);
	}

	public AkibotClient(String name, Component component, Integer port, InetSocketAddress parentSocketAddress) throws FailedClientConstructorException {
		log.debug(this + ": Initializing...");

		this.setName(name);
		this.setDaemon(true);
		this.component = component;

		try {
			this.socket = (port == null ? new DatagramSocket() : new DatagramSocket(port));
			// this.socket.setTrafficClass(0x04);
		} catch (SocketException e) {
			throw new FailedClientConstructorException(e);
		}

		try {
			NetworkUtils networkUtils = new NetworkUtils();
			this.myInetSocketAddress = new InetSocketAddress(networkUtils.getLocalIP(), socket.getLocalPort());
		} catch (Exception e) {
			throw new FailedClientConstructorException(e);
		}
		this.parentSocketAddress = parentSocketAddress;
		this.incommingMessageManager = new IncommingMessageManager(this);
		this.outgoingMessageManager = new OutgoingMessageManager(this);
		this.synchronizedMessageManager = new SynchronizedMessageManager(this);
		this.clientDescriptionList = new ArrayList<ClientDescription>();
		this.component.setAkibotClient(this);

		myClientDescription = new ClientDescription(name, component.getClass().getName(), getMyInetSocketAddress());

		if (getParentSocketAddress() != null) {
			dnsClientDescription = new ClientDescription(null, null, getParentSocketAddress());
		}
		log.info(this + ": initialized.");
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
			onClientDescriptionRequest((ClientDescriptionRequest) message);
		} else if (message instanceof ClientDescriptionResponse) {
			onClientDescriptionResponse((ClientDescriptionResponse) message);
		} else if (message instanceof StatusRequest) {
			onStatusRequest((StatusRequest) message);
		} else if (message instanceof GetConfigurationResponse) {
			this.component.onGetConfigurationResponse((GetConfigurationResponse) message);
		}
	}

	private void onClientDescriptionRequest(ClientDescriptionRequest clientDescriptionRequest) throws FailedToSendMessageException {
		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(this, clientDescriptionRequest.getClientDescription(), clientDescriptionList);
		ClientDescriptionResponse response = new ClientDescriptionResponse();
		response.setClientDescriptionList(clientDescriptionList);
		response.setSenderClientDescription(myClientDescription);
		outgoingMessageManager.broadcastMessage(response);
	}

	private void onClientDescriptionResponse(ClientDescriptionResponse clientDescriptionResponse) throws FailedToConfigureException {
		// log.trace("** onClientDescriptionResponse BEFORE: " + this + ": " + this.clientDescriptionList);
		clientDescriptionList = ClientDescriptionUtils.mergeList(this, clientDescriptionResponse.getClientDescriptionList(), clientDescriptionList);
		// log.trace("** onClientDescriptionResponse BEFORE2: " + this + ": " + this.clientDescriptionList);
		clientDescriptionList = ClientDescriptionUtils.mergeClientDescription(this, clientDescriptionResponse.getSenderClientDescription(),
				clientDescriptionList);
		// log.trace("** onClientDescriptionResponse AFTER: " + this + ": " + this.clientDescriptionList);
		if (!executedOnConfigurationClientAvailable && ClientDescriptionUtils.hasTopic(clientDescriptionList, new GetConfigurationRequest(), true)) {
			onConfigurationClientAvailable();
			executedOnConfigurationClientAvailable = true;
		}
	}

	private void onConfigurationClientAvailable() throws FailedToConfigureException {
		if (getComponent().getComponentConfiguration() == null) {
			try {
				GetConfigurationRequest getConfigurationRequest = new GetConfigurationRequest();
				getConfigurationRequest.setName(getName());
				getOutgoingMessageManager().broadcastMessage(getConfigurationRequest);
			} catch (Exception e) {
				throw new FailedToConfigureException(e);
			}
		}
	}

	private void onStatusRequest(StatusRequest statusRequest) throws FailedToSendMessageException {
		StatusResponse statusResponse = new StatusResponse();
		statusResponse.setComponentStatus(this.getComponent().getComponentStatus());
		statusResponse.copySyncId(statusRequest);
		outgoingMessageManager.broadcastMessage(statusResponse);
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
		log.trace(this + ": refreshClientDescriptionList: " + myClientDescription + " / " + clientDescriptionList);
		try {
			ClientDescriptionRequest clientDescriptionRequest = new ClientDescriptionRequest();
			clientDescriptionRequest.setClientDescription(myClientDescription);
			if (dnsClientDescription != null) {
				outgoingMessageManager.send(dnsClientDescription, clientDescriptionRequest);
			}
			// outgoingMessageManager.broadcastMessage(clientDescriptionRequest);
		} catch (FailedToSendMessageException e) {
			log.catching(this, e);
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

	/**
	 * Start AkiBotClient includes start of Thread, Component, Messaging threads, refreshing client list and loading of configuration
	 */
	@Override
	public void start() {
		try {
			log.debug(this + ": Starting AkibotClient...");

			super.start();
			component.loadDefaults();
			component.startComponent();
			incommingMessageManager.start();
			outgoingMessageManager.start();
			synchronizedMessageManager.start();

			refreshClientDescriptionList();

			log.debug(this + ": started and configured.");
		} catch (Exception e) {
			log.catching(this, e);
			log.error(this + ": Failed to start");
			this.interrupt();
		}
	}

	@Override
	public String toString() {
		return "[" + getName() + "]";
	}

}
