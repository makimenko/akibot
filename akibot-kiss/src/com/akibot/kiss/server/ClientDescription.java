package com.akibot.kiss.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.akibot.kiss.message.Message;

public class ClientDescription implements Serializable {
	private String name;
	private ArrayList<Message> topicList;

	public ClientDescription(String name) {
		this.name = name;
		this.topicList = new ArrayList<Message>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Message> getTopicList() {
		return topicList;
	}

	public void setTopicList(ArrayList<Message> topicList) {
		this.topicList = topicList;
	}

	public boolean isInterestedInMessage(Object obj) {
		ArrayList<Message> topicList = this.getTopicList();
		Iterator<Message> i = topicList.iterator();
		while (i.hasNext()) {
			Message topicMessage = (Message) i.next();
			if (topicMessage.getClass().isAssignableFrom(obj.getClass())) {
				return true;
			}
		}
		return false;
	}

}
