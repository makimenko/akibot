package com.akibot.engine2.network;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;

import com.akibot.engine2.message.Message;

public class ClientDescription implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private InetSocketAddress address;
	private ArrayList<Message> topicList;

	public ClientDescription(String name, InetSocketAddress address) {
		setName(name);
		setTopicList(new ArrayList<Message>());
		setAddress(address);
	}

	public String getName() {
		return name;
	}

	public ArrayList<Message> getTopicList() {
		return topicList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTopicList(ArrayList<Message> topicList) {
		this.topicList = topicList;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ClientDescription: ");
		sb.append(name);
		sb.append(" - ");
		if (address == null) {
			sb.append("N/A");
		} else {
			sb.append(address.getHostString());
			sb.append(":");
			sb.append(address.getPort());
		}
		sb.append(": ");
		if (topicList != null || topicList.size() > 0) {
			Iterator<Message> i = topicList.iterator();
			while (i.hasNext()) {
				Message msg = (Message) i.next();
				sb.append(msg);
				if (i.hasNext()) {
					sb.append(", ");
				}
			}
		} else {
			sb.append(" (no topics)");
		}
		return sb.toString();
	}

}
