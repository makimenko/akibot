package com.akibot.tanktrack.component.distance;

import java.util.Random;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class DistanceMeterComponent extends DefaultComponent {

	private DistanceResponse distanceResponse;

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof DistanceRequest) {
			DistanceRequest request = (DistanceRequest) message;
			Random randomGenerator = new Random();
			distanceResponse = new DistanceResponse();
			distanceResponse.setMeters(randomGenerator.nextDouble() * 4);
			if (request.getSyncId() != null) {

				distanceResponse.setSyncId(request.getSyncId());
			}

			this.getClient().send(distanceResponse);
		}
	}

	@Override
	public void run() {

	}

}
