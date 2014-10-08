package com.akibot.kiss.component;

import java.util.Random;

import com.akibot.kiss.message.Request;
import com.akibot.kiss.message.request.DistanceRequest;
import com.akibot.kiss.message.response.DistanceResponse;
import com.akibot.kiss.server.Client;

public class SimpleController implements Component {

	private DistanceResponse distanceStatus;

	@Override
	public void executeRequest(Client client, Request request) throws Exception {
		if (request instanceof DistanceRequest) {
			Random randomGenerator = new Random();
			distanceStatus = new DistanceResponse();
			distanceStatus.setMeters(randomGenerator.nextDouble() * 4);
			client.send(distanceStatus);
		}
	}

}
