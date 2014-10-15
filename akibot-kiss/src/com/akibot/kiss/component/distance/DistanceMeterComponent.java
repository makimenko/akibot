package com.akibot.kiss.component.distance;

import java.util.Random;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Request;
import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.message.response.DistanceResponse;

public class DistanceMeterComponent extends DefaultComponent {

	private DistanceResponse distanceStatus;

	@Override
	public void executeRequest(Request request) throws Exception {

		if (request instanceof DistanceRequest) {
			Random randomGenerator = new Random();
			distanceStatus = new DistanceResponse();
			distanceStatus.setMeters(randomGenerator.nextDouble() * 4);
			this.getClient().send(distanceStatus);
		}
	}

}
