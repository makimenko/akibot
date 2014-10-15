package com.akibot.kiss.component.distance;

import java.util.Random;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.message.response.DistanceResponse;

public class DistanceMeterComponent extends DefaultComponent {

	private DistanceResponse distanceResponse;

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof DistanceRequest) {
			Random randomGenerator = new Random();
			distanceResponse = new DistanceResponse();
			distanceResponse.setMeters(randomGenerator.nextDouble() * 4);
			this.getClient().send(distanceResponse);
		}
	}

}
