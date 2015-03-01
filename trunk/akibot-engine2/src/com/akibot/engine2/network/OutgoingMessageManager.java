package com.akibot.engine2.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;

public class OutgoingMessageManager {
	private static final Logger log = LogManager.getLogger(OutgoingMessageManager.class.getName());

	private AkibotClient akibotClient;

	public OutgoingMessageManager(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
	}

	public void broadcastMessage(Message message) throws FailedToSendMessageException {
		if (akibotClient.getClientDescriptionList() != null && akibotClient.getClientDescriptionList().size() > 0) {
			log.trace(akibotClient + ": broadcastMessage: " + message);
			for (ClientDescription client : akibotClient.getClientDescriptionList()) {
				if (ClientDescriptionUtils.isSystemMessage(message) || ClientDescriptionUtils.isInterestedInMessage(client, message)) {
					send(client, message);
				}
			}
		} else {
			log.trace(akibotClient + ": broadcastMessage: Skip broadcasting. No Clients!");
		}
	}

	private byte[] messageToByte(Message message) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		oos.flush();
		return baos.toByteArray();
	}

	public void send(ClientDescription clientDescription, Message message) throws FailedToSendMessageException {
		send(clientDescription.getAddress(), message);
	}

	public void send(InetSocketAddress inetSocketAddress, Message message) throws FailedToSendMessageException {
		log.trace(akibotClient + ": send: to=(" + inetSocketAddress.getHostString() + ":" + inetSocketAddress.getPort() + "): "
				+ message);
		try {
			message.setFrom(akibotClient.getName());
			byte[] buf;
			buf = messageToByte(message);
			DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, inetSocketAddress);
			akibotClient.getSocket().send(datagramPacket);
		} catch (IOException e) {
			log.catching(e);
			throw new FailedToSendMessageException();
		}
	}

	public Response sendSyncRequest(Request request, int timeout) throws FailedToSendMessageException {
		Request newRequest;
		try {
			SynchronizedMessageManager sync = akibotClient.getSynchronizedMessageManager();
			newRequest = sync.enrichRequest(request);

			log.trace(akibotClient + ": Sync messasge sent: " + newRequest + " (syncId=" + newRequest.getSyncId() + ")");
			broadcastMessage(newRequest);

			synchronized (sync.getSyncId()) {
				sync.getSyncId().wait(timeout);
			}

			if (sync.getSyncResponse() == null) {
				throw new Exception("Timeout occured while waiting sync response");
			} else {
				log.trace(akibotClient + ": Sync messasge received: " + sync.getSyncResponse().getSyncId() + ": " + sync.getSyncResponse());
			}
			return sync.getSyncResponse();

		} catch (Exception e) {
			log.catching(e);
			throw new FailedToSendMessageException();
		}
	}

	public void start() {

	}
}
