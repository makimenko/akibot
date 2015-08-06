package com.akibot.web.bean;

import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.network.ClientDescription;

public class BeanUtils {

	public static SimplifiedClientDescription simplifyClientDescription(ClientDescription clientDescription) {
		SimplifiedClientDescription simplifiedClientDescription = new SimplifiedClientDescription();
		simplifiedClientDescription.setName(clientDescription.getName());
		simplifiedClientDescription.setAddress(clientDescription.getAddress().getHostString() + ":" + clientDescription.getAddress().getPort());
		simplifiedClientDescription.setComponentClassName(clientDescription.getComponentClassName());
		return simplifiedClientDescription;
	}

	public static List<SimplifiedClientDescription> simplifyClientDescriptionList(List<ClientDescription> list) {
		List<SimplifiedClientDescription> result = new ArrayList<SimplifiedClientDescription>();
		for (ClientDescription descr : list) {
			result.add(simplifyClientDescription(descr));
		}
		return result;
	}
}
