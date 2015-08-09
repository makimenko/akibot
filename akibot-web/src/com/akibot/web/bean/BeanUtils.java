package com.akibot.web.bean;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.network.ClientDescription;

public class BeanUtils {

	public static SimplifiedClientDescription simplifyClientDescription(ClientDescription clientDescription, ComponentStatus componentStatus) {
		SimplifiedClientDescription simplifiedClientDescription = new SimplifiedClientDescription();
		simplifiedClientDescription.setName(clientDescription.getName());
		simplifiedClientDescription.setAddress(clientDescription.getAddress().getHostString() + ":" + clientDescription.getAddress().getPort());
		simplifiedClientDescription.setComponentClassName(clientDescription.getComponentClassName());
		if (componentStatus == null) {
			simplifiedClientDescription.setStatus("unknown");
		} else {
			simplifiedClientDescription.setStatus((componentStatus.isReady() ? "Ready" : "Not Ready"));
		}
		return simplifiedClientDescription;
	}

}
