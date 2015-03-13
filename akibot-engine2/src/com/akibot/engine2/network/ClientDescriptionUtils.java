package com.akibot.engine2.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.message.SystemResponse;

public class ClientDescriptionUtils {
	private static final AkiLogger log = AkiLogger.create(ClientDescriptionUtils.class);

	public static boolean equalAddress(ClientDescription clientDescriptionA, ClientDescription clientDescriptionB) {
		if (clientDescriptionA == null || clientDescriptionB == null) {
			return false;
		} else {
			InetSocketAddress a = clientDescriptionA.getAddress();
			InetSocketAddress b = clientDescriptionB.getAddress();
			return a.getHostString().equalsIgnoreCase(b.getHostString()) && a.getPort() == b.getPort();
		}
	}

	public static boolean equalName(ClientDescription clientDescriptionA, ClientDescription clientDescriptionB) {
		if (clientDescriptionA == null || clientDescriptionB == null || clientDescriptionA.getName() == null) {
			return false;
		} else {
			return clientDescriptionA.getName().equals(clientDescriptionB.getName());
		}
	}

	public static int findByAddress(List<ClientDescription> list, ClientDescription clientDescription) {
		int index = -1;
		for (ClientDescription descr : list) {
			index++;
			if (equalAddress(descr, clientDescription)) {
				return index;
			}
		}
		return -1;
	}

	public static int findByName(List<ClientDescription> list, ClientDescription clientDescription) {
		int index = -1;
		for (ClientDescription descr : list) {
			index++;
			if (equalName(descr, clientDescription)) {
				return index;
			}
		}
		return -1;
	}

	public static boolean isInterestedInMessage(ClientDescription clientDescription, Message message) {
		ArrayList<Message> topicList = clientDescription.getTopicList();
		if (topicList == null || topicList.size() == 0) {
			return false;
		} else {
			for (Message topicMessage : topicList) {
				if (topicMessage.getClass().isAssignableFrom(message.getClass())) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isSystemMessage(Message message) {
		return (message instanceof SystemRequest || message instanceof SystemResponse);
	}

	public static List<ClientDescription> mergeClientDescription(ClientDescription myClientDescription, ClientDescription clientDescription,
			List<ClientDescription> mergeTo) {
		if (clientDescription == null) {
			return mergeTo;
		} else if (mergeTo == null) {
			mergeTo = new ArrayList<ClientDescription>();
			mergeTo.add(clientDescription);
			return mergeTo;
		} else if (mergeTo.size() == 0) {
			mergeTo.add(clientDescription);
			return mergeTo;
		} else {
			int addressIndex = findByAddress(mergeTo, clientDescription);
			int nameIndex = findByName(mergeTo, clientDescription);

			if (addressIndex < 0 && nameIndex < 0) {
				// new client
				log.trace(myClientDescription.getName() + ": Add client: " + clientDescription);
				mergeTo.add(clientDescription);
			} else if (addressIndex > 0 && nameIndex < 0) {
				// Change name
				log.trace(myClientDescription.getName() + ": Change client name: " + clientDescription);
				mergeTo.remove(addressIndex);
				mergeTo.add(clientDescription);
			} else if (addressIndex < 0 && nameIndex > 0) {
				// Change address
				log.trace(myClientDescription.getName() + ": Change client address: " + clientDescription);
				mergeTo.remove(nameIndex);
				mergeTo.add(clientDescription);
			}
			return mergeTo;
		}
	}

	public static List<ClientDescription> mergeList(ClientDescription myClientDescription, List<ClientDescription> mergeFrom,
			List<ClientDescription> mergeTo) {
		log.trace(myClientDescription.getName() + ": Merge clients (" + myClientDescription.getName() + "): " + mergeFrom + " -> " + mergeTo);
		if (mergeFrom == null || mergeFrom.size() == 0) {
			return mergeTo;
		} else if (mergeTo == null || mergeTo.size() == 0) {
			return mergeFrom;
		} else {
			for (ClientDescription descr : mergeFrom) {
				mergeTo = mergeClientDescription(myClientDescription, descr, mergeTo);
			}
			return mergeTo;
		}
	}

}
