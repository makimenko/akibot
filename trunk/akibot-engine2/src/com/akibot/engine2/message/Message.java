package com.akibot.engine2.message;

import java.io.Serializable;

public class Message implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String from;
	private String syncId;
	private String to;

	@Override
	public Message clone() throws CloneNotSupportedException {
		return (Message) super.clone();
	}

	public void copySyncId(Message requestMessage) {
		if (requestMessage != null && requestMessage.getSyncId() != null) {
			this.syncId = requestMessage.getSyncId();
		}
	}

	public String getFrom() {
		return from;
	}

	public String getSyncId() {
		return syncId;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
