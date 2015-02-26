package com.akibot.engine2.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.Component;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;

public class AkibotClient extends Thread {
	private static final Logger log = LogManager.getLogger(AkibotClient.class.getName());
	private DatagramSocket socket;

	private IncommingMessageManager incommingMessageManager;
	private Component component;
	private InetSocketAddress myInetSocketAddress;
	private InetSocketAddress parentSocketAddress;

	public InetSocketAddress getMyInetSocketAddress() {
		return myInetSocketAddress;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public InetSocketAddress getParentSocketAddress() {
		return parentSocketAddress;
	}

	public AkibotClient(Component component, Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		log.debug("Initializing...");
		this.setDaemon(true);
		this.component = component;
		this.socket = (port == null ? new DatagramSocket() : new DatagramSocket(port));
		// this.socket.setTrafficClass(0x04);
		this.myInetSocketAddress = new InetSocketAddress(socket.getLocalAddress().getLocalHost(), socket.getLocalPort());
		this.parentSocketAddress = parentSocketAddress;

		incommingMessageManager = new IncommingMessageManager(this);

		this.component.setAkibotNode(this);
		log.info(component + ": initialized.");
	}

	public AkibotClient(Component component, int port) throws SocketException, UnknownHostException {
		this(component, port, null);
	}

	public AkibotClient(Component component, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this(component, null, parentSocketAddress);
	}

	public void start() {
		log.debug(component + ": Starting AkibotClient...");
		super.start();
		component.start();
		incommingMessageManager.start();
		log.debug(component + ": started.");
	}

	private byte[] messageToByte(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		oos.flush();
		return baos.toByteArray();
	}

	public void send(InetSocketAddress inetSocketAddress, Message message) throws FailedToSendMessageException {
		log.trace(component + ": send: to=(" + inetSocketAddress.getHostString() + ":" + inetSocketAddress.getPort() + "): " + message);
		try {
			message.setFrom(component.toString());
			byte[] buf;
			buf = messageToByte(message);
			DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, inetSocketAddress);
			socket.send(datagramPacket);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new FailedToSendMessageException();
		}
	}

	public void send(ClientDescription clientDescription, Message message) throws FailedToSendMessageException {
		send(clientDescription.getAddress(), message);
	}

	public Component getComponent() {
		return component;
	}

	public String printClients() {
		StringBuffer sb = new StringBuffer();
		List list = component.getClientDescriptionList();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			ClientDescription client = (ClientDescription) i.next();
			sb.append(client);
			sb.append("\n");
		}
		return sb.toString();
	}

}
