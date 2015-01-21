package com.akibot.engine2.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.akibot.engine2.message.Message;

public class AkibotNode {
	private DatagramSocket childSocket;
	private DatagramSocket parentSocket;
	private LinkedBlockingQueue<Message> messages;

	public AkibotNode(int port, InetSocketAddress parentSocketAddress)
			throws SocketException, UnknownHostException {
		this.childSocket = new DatagramSocket(port);
		this.parentSocket = new DatagramSocket();
		this.parentSocket.connect(parentSocketAddress);

	}

	public void start() {
		
		new Thread() {
			public void run() {
				byte[] buf = new byte[1000];
				DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

				while (!this.isInterrupted()) {
					try {
						childSocket.receive(inDatagramPacket);
						Message message = byteToMessage(inDatagramPacket.getData());
						messages.put(message);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}.run();
		
		
	}

	public void send(DatagramSocket socket, String message) throws IOException {
		byte[] buf = message.getBytes();
		DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
		socket.send(datagramPacket);
	}

	private byte[] messageToByte(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		oos.flush();
		return baos.toByteArray();
	}

	private Message byteToMessage(byte[] byteArray) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
		ObjectInputStream oos = new ObjectInputStream(baos);
		return (Message) oos.readObject();
	}
}
