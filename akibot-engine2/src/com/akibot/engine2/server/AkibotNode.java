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

import com.akibot.engine2.message.Message;

public class AkibotNode extends Thread {
	private DatagramSocket socket;
	private DatagramSocket parentSocket;
	private BlockingQueue<Message> messageQueue;
	private IncommingMessageHandler incommingMessageHandler;
	private MessageQueueHandler messageQueueHandler;

	public AkibotNode(Integer port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this.setDaemon(true);
		this.socket = (port==null? new DatagramSocket() : new DatagramSocket(port) );
		this.parentSocket = new DatagramSocket();
		if (parentSocketAddress != null) {
			this.parentSocket.connect(parentSocketAddress);
		}
		this.messageQueue = new LinkedBlockingQueue<Message>();
		this.incommingMessageHandler = new IncommingMessageHandler(socket, messageQueue);
		this.messageQueueHandler = new MessageQueueHandler(this, messageQueue);
		
	}

	public void start() {
		super.start();
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

	private void send(DatagramSocket socket, Message message) throws IOException {
		byte[] buf = messageToByte(message);
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
		socket.send(datagramPacket);
	}

	private void send(Message message) throws IOException {
		// TODO: Identify recipients
		send(socket, message);
	}

	protected void onMessageReceived(Message message) {
		// Do some action
	}

}
