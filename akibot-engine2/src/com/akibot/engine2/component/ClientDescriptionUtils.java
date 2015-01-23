package com.akibot.engine2.component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.SystemRequest;
import com.akibot.engine2.message.SystemResponse;

public class ClientDescriptionUtils {

	public static List<ClientDescription> merge(List<ClientDescription> mergeFrom, List<ClientDescription> mergeTo) {
		if (mergeFrom == null || mergeFrom.size() == 0) {
			return mergeTo;
		} else if (mergeTo == null || mergeTo.size() == 0) {
			return mergeFrom;
		} else {
			Iterator<ClientDescription> i = mergeFrom.iterator();
			while (i.hasNext()) {
				ClientDescription descr = (ClientDescription) i.next();
				if (!existsClient(mergeTo, descr)) {
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
			if (equalClients(descr, clientDescription)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	public static boolean equalClients(ClientDescription clientDescriptionA, ClientDescription clientDescriptionB) {
		if (clientDescriptionA == null || clientDescriptionB == null) {
			return false;
		} else {
			InetSocketAddress a = clientDescriptionA.getAddress();
			InetSocketAddress b = clientDescriptionB.getAddress();
			return a.getHostString().equalsIgnoreCase(b.getHostString()) && a.getPort() == b.getPort();
		}
	}

	public static boolean isInterestedInMessage(ClientDescription clientDescription, Message message) {
		ArrayList<Message> topicList = clientDescription.getTopicList();
		if (topicList == null || topicList.size() == 0) {
			return true;
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
