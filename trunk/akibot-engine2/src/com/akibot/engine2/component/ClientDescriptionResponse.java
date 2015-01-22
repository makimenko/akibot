package com.akibot.engine2.component;

import java.util.Iterator;
import java.util.List;

import com.akibot.engine2.message.Response;
import com.akibot.engine2.message.SystemResponse;
import com.akibot.engine2.server.ClientDescription;

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

	public List<ClientDescription> mergeNewInto(List<ClientDescription> list) {
		if (clientDescriptionList == null || clientDescriptionList.size() == 0) {
			return list;
		}
		if (list == null || list.size() == 0) {
			return clientDescriptionList;
		} else {
			Iterator<ClientDescription> i = clientDescriptionList.iterator();
			while (i.hasNext()) {
				ClientDescription descr = (ClientDescription) i.next();
				if (!existsClient(list, descr)) {
					list.add(descr);
				}
			}
			return list;
		}
	}

	public boolean existsClient(List<ClientDescription> list, ClientDescription clientDescription) {
		boolean exists = false;
		Iterator<ClientDescription> i = list.iterator();
		while (i.hasNext()) {
			ClientDescription descr = (ClientDescription) i.next();
			if (descr.getName().equals(clientDescription.getName())) {
				exists = true;
				break;
			}
		}
		return exists;
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
