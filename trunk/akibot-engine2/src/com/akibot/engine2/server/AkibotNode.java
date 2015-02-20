package com.akibot.engine2.server;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.ClientDescription;
import com.akibot.engine2.component.Component;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;

public class AkibotNode extends Thread {
	private static final Logger log = LogManager.getLogger(AkibotNode.class.getName());
	private DatagramSocket socket;
	private BlockingQueue<Message> messageQueue;
	private IncommingMessageHandler incommingMessageHandler;
	private MessageQueueHandler messageQueueHandler;
	private Component component;
	private InetSocketAddress myInetSocketAddress;
	private InetSocketAddress parentSocketAddress;

	public InetSocketAddress getMyInetSocketAddress() {
		return myInetSocketAddress;
	}

	public InetSocketAddress getParentSocketAddress() {
		return parentSocketAddress;
	}

	public AkibotNode(Component component, Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		log.debug("Initializing...");
		this.setDaemon(true);
		this.component = component;
		this.socket = (port == null ? new DatagramSocket() : new DatagramSocket(port));
		// this.socket.setTrafficClass(0x04);
		this.myInetSocketAddress = new InetSocketAddress(socket.getLocalAddress().getLocalHost(), socket.getLocalPort());
		this.parentSocketAddress = parentSocketAddress;
		this.messageQueue = new LinkedBlockingQueue<Message>();
		this.incommingMessageHandler = new IncommingMessageHandler(socket, messageQueue);
		this.messageQueueHandler = new MessageQueueHandler(this, messageQueue);
		this.component.setAkibotNode(this);
		log.debug(component + ": initialized.");
	}

	public AkibotNode(Component component, int port) throws SocketException, UnknownHostException {
		this(component, port, null);
	}

	public AkibotNode(Component component, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this(component, null, parentSocketAddress);
	}

	public void start() {
		log.debug(component + ": Starting...");
		super.start();
		component.start();
		incommingMessageHandler.start();
		messageQueueHandler.start();
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
