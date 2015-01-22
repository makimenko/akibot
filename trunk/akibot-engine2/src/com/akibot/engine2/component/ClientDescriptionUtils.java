package com.akibot.engine2.component;

import java.util.Iterator;
import java.util.List;

public class ClientDescriptionUtils {

	public List<ClientDescription> merge(List<ClientDescription> mergeFrom, List<ClientDescription> mergeTo) {
		if (mergeFrom == null || mergeFrom.size() == 0) {
			return mergeTo;
		}
		if (mergeTo == null || mergeTo.size() == 0) {
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

}
