package com.akibot.engine2.component;

import java.util.Iterator;
import java.util.List;

import com.akibot.engine2.message.SystemResponse;

public class ClientDescriptionResponse extends SystemResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ClientDescription> clientDescriptionList;

	public List<ClientDescription> getClientDescriptionList() {
		return clientDescriptionList;
	}

	public void setClientDescriptionList(List<ClientDescription> clientDescriptionList) {
		this.clientDescriptionList = clientDescriptionList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Iterator<ClientDescription> i = clientDescriptionList.iterator();
		while (i.hasNext()) {
			ClientDescription descr = (ClientDescription) i.next();
			sb.append(descr);
			sb.append("\n");
		}
		return sb.toString();
	}

}
