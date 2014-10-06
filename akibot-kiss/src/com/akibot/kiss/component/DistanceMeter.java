package com.akibot.kiss.component;

import java.util.Random;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceMeterCommandMessage;
import com.akibot.kiss.message.DistanceMeterStatusMessage;
import com.akibot.kiss.server.Client;

public class DistanceMeter implements Component {

	private DistanceMeterStatusMessage distanceStatus;

	@Override
	public void executeCommand(Client client, CommandMessage commandMessage) throws Exception {
		if (commandMessage instanceof DistanceMeterCommandMessage) {
			Random randomGenerator = new Random();
			distanceStatus = new DistanceMeterStatusMessage();
			distanceStatus.setMeters(randomGenerator.nextDouble()*4);
			client.send(distanceStatus);
		}
	}


}
