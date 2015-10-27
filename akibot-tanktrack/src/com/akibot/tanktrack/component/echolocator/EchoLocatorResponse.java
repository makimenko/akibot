package com.akibot.tanktrack.component.echolocator;

import com.akibot.engine2.message.Response;

public class EchoLocatorResponse extends Response {
	private static final long serialVersionUID = 1L;

	private MultipleDistanceDetails multipleDistanceDetails;

	public MultipleDistanceDetails getMultipleDistanceDetails() {
		return multipleDistanceDetails;
	}

	public void setMultipleDistanceDetails(MultipleDistanceDetails multipleDistanceDetails) {
		this.multipleDistanceDetails = multipleDistanceDetails;
	}

}
