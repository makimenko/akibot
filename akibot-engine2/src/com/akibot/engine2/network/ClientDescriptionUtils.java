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

	public static boolean hasTopic(List<ClientDescription> list, Message topic, boolean exactMatch) {
		for (ClientDescription descr : list) {
			if (isInterestedInMessage(descr, topic, exactMatch)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isInterestedInMessage(ClientDescription clientDescription, Message message) {
		return isInterestedInMessage(clientDescription, message, false);
	}

	public static boolean isInterestedInMessage(ClientDescription clientDescription, Message message, boolean exactMatch) {
		ArrayList<Message> topicList = clientDescription.getTopicList();
		if (message == null || topicList == null || topicList.size() == 0) {
			return false;
		} else {
			for (Message topicMessage : topicList) {
				if (exactMatch && topicMessage.getClass().equals(message.getClass())) {
					return true;
				} else if (!exactMatch && topicMessage.getClass().isAssignableFrom(message.getClass())) {
					return true;
				}
			}
			return false;
		}
	}

	public static boolean isAddressedToClient(ClientDescription clientDescription, Message message) {
		if (message == null || message.getTo() == null || message.getTo().length() == 0) {
			return true;
		} else {
			return message.getTo().equals(clientDescription.getName());
		}
	}

	public static boolean isSystemMessage(Message message) {
		return (message instanceof SystemRequest || message instanceof SystemResponse);
	}

	public static boolean isNew(ClientDescription clientDescriptionA, ClientDescription clientDescriptionB) {
		return clientDescriptionA.getStartupTime() > clientDescriptionB.getStartupTime() || clientDescriptionB.getName() != null;
	}

	public static List<ClientDescription> mergeClientDescription(AkibotClient akibotClient, ClientDescription clientDescription, List<ClientDescription> mergeTo) {
		if (clientDescription == null) {
			return mergeTo;
		} else if (equalAddress(akibotClient.getMyClientDescription(), clientDescription)
				&& equalName(akibotClient.getMyClientDescription(), clientDescription)) {
			// Skip my address
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
				log.trace(akibotClient + ": Add client: " + clientDescription);
				mergeTo.add(clientDescription);
			} else if (addressIndex > 0 && nameIndex < 0 && isNew(clientDescription, mergeTo.get(addressIndex))) {
				// Change name
				log.trace(akibotClient + ": Change client name: " + clientDescription);
				mergeTo.remove(addressIndex);
				mergeTo.add(clientDescription);
			} else if (addressIndex < 0 && nameIndex > 0 && isNew(clientDescription, mergeTo.get(nameIndex))) {
				// Change address
				log.trace(akibotClient + ": Change client address: " + clientDescription);
				mergeTo.remove(nameIndex);
				mergeTo.add(clientDescription);
			}
			return mergeTo;
		}
	}

	public static List<ClientDescription> mergeList(AkibotClient akibotClient, List<ClientDescription> mergeFrom, List<ClientDescription> mergeTo) {
		log.trace(akibotClient + ": Merge clients: " + mergeFrom + " -> " + mergeTo);
		if (mergeFrom == null || mergeFrom.size() == 0) {
			return mergeTo;
		} else if (mergeTo == null || mergeTo.size() == 0) {
			return mergeFrom;
		} else {
			for (ClientDescription descr : mergeFrom) {
				mergeTo = mergeClientDescription(akibotClient, descr, mergeTo);
			}
			return mergeTo;
		}
	}

}
