package com.akibot.engine2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AkibotNode {
	private DatagramSocket childSocket;
	private DatagramSocket parentSocket;

	public AkibotNode(int port, InetSocketAddress parentSocketAddress) throws SocketException, UnknownHostException {
		this.childSocket = new DatagramSocket(port);
		this.parentSocket = new DatagramSocket();
		this.parentSocket.connect(parentSocketAddress);
	}

	public void sendToParent(String message) throws IOException {
		byte[] buf = message.getBytes();
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
		parentSocket.send(datagramPacket);
	}

	public void broadcastToChilds(String message) throws IOException {
		byte[] buf = message.getBytes();
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
		childSocket.send(datagramPacket);
	}

}
