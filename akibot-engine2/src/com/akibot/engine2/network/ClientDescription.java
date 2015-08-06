package com.akibot.engine2.network;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import com.akibot.engine2.message.Message;

public class ClientDescription implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InetSocketAddress address;
	private String name;
	private ArrayList<Message> topicList;
	private long startupTime;
	private String componentClassName;

	public ClientDescription(String name, String componentClassName, InetSocketAddress address) {
		setName(name);
		setTopicList(new ArrayList<Message>());
		setAddress(address);
		setStartupTime(System.currentTimeMillis());
		setComponentClassName(componentClassName);
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Message> getTopicList() {
		return topicList;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTopicList(ArrayList<Message> topicList) {
		this.topicList = topicList;
	}

	public long getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(long startupTime) {
		this.startupTime = startupTime;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ClientDescription: ");
		sb.append(name);
		sb.append(" (");
		sb.append(componentClassName);
		sb.append(") - ");
		if (address == null) {
			sb.append("N/A");
		} else {
			sb.append(address.getHostString());
			sb.append(":");
			sb.append(address.getPort());
		}
		sb.append(": ");
		if (topicList != null || topicList.size() > 0) {
			for (Message msg : topicList) {
				sb.append(msg);
				sb.append(", ");
			}
		} else {
			sb.append(" (no topics)");
		}
		return sb.toString();
	}

	public String getComponentClassName() {
		return componentClassName;
	}

	public void setComponentClassName(String componentClassName) {
		this.componentClassName = componentClassName;
	}

}
