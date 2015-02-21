package com.akibot.engine2.component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.message.SystemResponse;

public class ClientDescriptionUtils {
	private static final Logger log = LogManager.getLogger(ClientDescriptionUtils.class.getName());

	public static List<ClientDescription> merge(ClientDescription myClientDescription, List<ClientDescription> mergeFrom,
			List<ClientDescription> mergeTo) {
		log.trace("Merge clients (" + myClientDescription.getName() + "): " + mergeFrom + " -> " + mergeTo);
		if (mergeFrom == null || mergeFrom.size() == 0) {
			return mergeTo;
		} else if (mergeTo == null || mergeTo.size() == 0) {
			return mergeFrom;
		} else {
			Iterator<ClientDescription> i = mergeFrom.iterator();
			while (i.hasNext()) {
				ClientDescription descr = (ClientDescription) i.next();
				if (!equalName(myClientDescription, descr) && !existsClient(mergeTo, descr)) {
					log.trace("Add client: " + descr);
					mergeTo.add(descr);
				}
			}
			return mergeTo;
		}
	}

	public static List<ClientDescription> merge(ClientDescription clientDescription, List<ClientDescription> mergeTo) {
		if (clientDescription == null) {
			return mergeTo;
		} else if (mergeTo == null) {
			mergeTo = new ArrayList<ClientDescription>();
			mergeTo.add(clientDescription);
			return mergeTo;
		} else if (mergeTo.size() == 0 || !existsClient(mergeTo, clientDescription)) {
			mergeTo.add(clientDescription);
			return mergeTo;
		} else {
			return mergeTo;
		}
	}

	public static boolean existsClient(List<ClientDescription> list, ClientDescription clientDescription) {
		boolean exists = false;
		Iterator<ClientDescription> i = list.iterator();
		while (i.hasNext()) {
			ClientDescription descr = (ClientDescription) i.next();
			if (equalAddress(descr, clientDescription)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

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
		if (clientDescriptionA == null || clientDescriptionB == null) {
			return false;
		} else {
			return clientDescriptionA.getName().equals(clientDescriptionB.getName());
		}
	}

	public static boolean isInterestedInMessage(ClientDescription clientDescription, Message message) {
		ArrayList<Message> topicList = clientDescription.getTopicList();
		if (topicList == null || topicList.size() == 0) {
			return false;
		} else {
			Iterator<Message> i = topicList.iterator();
			while (i.hasNext()) {
				Message topicMessage = i.next();
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

}
