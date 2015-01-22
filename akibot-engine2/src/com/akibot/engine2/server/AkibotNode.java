package com.akibot.engine2.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.akibot.engine2.component.Component;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;

public class AkibotNode extends Thread {
	private DatagramSocket socket;
	private DatagramSocket parentSocket;
	private BlockingQueue<Message> messageQueue;
	private IncommingMessageHandler incommingMessageHandler;
	private MessageQueueHandler messageQueueHandler;
	private Component component;

	public AkibotNode(Component component, Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this.setDaemon(true);
		this.component = component;
		this.component.setAkibotNode(this);
		this.socket = (port == null ? new DatagramSocket() : new DatagramSocket(port));
		this.parentSocket = new DatagramSocket();
		if (parentSocketAddress != null) {
			this.parentSocket.connect(parentSocketAddress);
		}
		this.messageQueue = new LinkedBlockingQueue<Message>();
		this.incommingMessageHandler = new IncommingMessageHandler(socket, messageQueue);
		this.messageQueueHandler = new MessageQueueHandler(this, messageQueue);
	}

	public AkibotNode(Component component, int port) throws SocketException, UnknownHostException {
		this(component, port, null);
	}

	public AkibotNode(Component component, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this(component, null, parentSocketAddress);
	}

	public void start() {
		super.start();
		component.start();
		incommingMessageHandler.start();
		messageQueueHandler.start();
	}

	private byte[] messageToByte(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		oos.flush();
		return baos.toByteArray();
	}

	public void send(DatagramSocket socket, Message message) throws FailedToSendMessageException {
		try {
			byte[] buf;
			buf = messageToByte(message);
			DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
			socket.send(datagramPacket);
		} catch (IOException e) {
			throw new FailedToSendMessageException();
		}
	}

	public void send(Message message) throws FailedToSendMessageException {
		// TODO: Identify recipients
		send(socket, message);
	}

	public Component getComponent() {
		return component;
	}

}
