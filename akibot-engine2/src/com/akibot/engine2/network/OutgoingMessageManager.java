package com.akibot.engine2.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Request;
import com.akibot.engine2.message.Response;

public class OutgoingMessageManager {
	private static final AkiLogger log = AkiLogger.create(OutgoingMessageManager.class);

	private AkibotClient akibotClient;

	public OutgoingMessageManager(AkibotClient akibotClient) {
		this.akibotClient = akibotClient;
	}

	public void broadcastMessage(Message message) throws FailedToSendMessageException {
		if (message == null) {
			throw new FailedToSendMessageException();
		} else {
			int count = 0;
			if (akibotClient.getClientDescriptionList() != null && akibotClient.getClientDescriptionList().size() > 0) {
				log.trace(akibotClient + ": broadcastMessage: " + message);
				// boolean advancedDebug = true;
				// //akibotClient.toString().equals("[akibot.test]") && message
				// instanceof TestResponse;
				// if (advancedDebug) {
				// log.trace(akibotClient + ": !!!: getClientDescriptionList: " +
				// akibotClient.getClientDescriptionList());
				// }
				for (ClientDescription client : akibotClient.getClientDescriptionList()) {
					// if (advancedDebug) {
					// log.trace(akibotClient + ": !!!: client: " + client+
					// ": isSystemMessage="+ClientDescriptionUtils.isSystemMessage(message)+", isAddressedToClient="+ClientDescriptionUtils.isAddressedToClient(client,
					// message)+", isInterestedInMessage="+
					// ClientDescriptionUtils.isInterestedInMessage(client,
					// message)+" / to="+message.getTo());
					// }
					if (ClientDescriptionUtils.isSystemMessage(message)
							|| (ClientDescriptionUtils.isAddressedToClient(client, message) && ClientDescriptionUtils.isInterestedInMessage(client, message))) {
						count++;
						send(client, message);
					}
				}
			} else {
				log.warn(akibotClient + ": broadcastMessage: Skip broadcasting. No Clients!");
			}
			if (count == 0) {
				log.warn(akibotClient + ": broadcastMessage: Noone interested in: " + message + " (to=" + message.getTo()
						+ "); akibotClient.clientDescriptionList=(" + akibotClient.getClientDescriptionList() + ")");
			}
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
		Message msg;
		try {
			msg = message.clone();
		} catch (CloneNotSupportedException e1) {
			log.catching(akibotClient, e1);
			throw new FailedToSendMessageException();
		}

		String host = clientDescription.getAddress().getHostString();
		int port = clientDescription.getAddress().getPort();
		String to = clientDescription.getName();

		msg.setFrom(akibotClient.getName());
		msg.setTo(to);
		log.msg(akibotClient, msg);

		try {
			msg.setFrom(akibotClient.getName());
			byte[] buf;
			buf = messageToByte(msg);
			InetSocketAddress address = new InetSocketAddress(host, port);
			DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, address);
			akibotClient.getSocket().send(datagramPacket);
		} catch (IOException e) {
			log.catching(akibotClient, e);
			throw new FailedToSendMessageException();
		}
	}

	public Response sendSyncRequest(Request request, int timeout) throws FailedToSendMessageException {
		if (request != null) {
			Request newRequest;
			try {
				SynchronizedMessageManager sync = akibotClient.getSynchronizedMessageManager();
				newRequest = sync.enrichRequest(request);

				log.trace(akibotClient + ": Sync messasge sent: " + newRequest + " (syncId=" + newRequest.getSyncId() + ")");
				broadcastMessage(newRequest);

				synchronized (sync.getSyncId()) {
					sync.getSyncId().wait(timeout);
				}

				sync.setSyncId(null); // release sync id

				if (sync.getSyncResponse() == null) {
					throw new Exception("Timeout occured while waiting sync response");
				} else {
					log.trace(akibotClient + ": Sync messasge received: " + sync.getSyncResponse().getSyncId() + ": " + sync.getSyncResponse());
				}
				return sync.getSyncResponse();

			} catch (Exception e) {
				log.catching(akibotClient, e);
				throw new FailedToSendMessageException();
			}
		} else {
			throw new FailedToSendMessageException();
		}
	}

	public void start() {

	}
}
