package com.akibot.kiss.component;

import java.util.Random;
import com.akibot.kiss.message.Command;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;
import com.akibot.kiss.server.Client;

public class DistanceMeter extends DefaultComponent {

	private DistanceStatusMessage distanceStatus;


	@Override
	public void executeCommand(Client client, CommandMessage commandMessage) throws Exception {
		if (commandMessage.getCommand() == Command.GET_DISTANCE) {
			Random randomGenerator = new Random();
			distanceStatus = new DistanceStatusMessage();
			distanceStatus.setMeters(randomGenerator.nextDouble()*4);
			client.send(distanceStatus);
		}
	}


}
