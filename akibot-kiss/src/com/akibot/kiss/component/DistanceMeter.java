package com.akibot.kiss.component;

import java.io.IOException;
import java.util.Random;

import com.akibot.kiss.message.Command;
import com.akibot.kiss.message.CommandMessage;
import com.akibot.kiss.message.DistanceStatusMessage;

public class DistanceMeter extends DefaultComponent {

	private DistanceStatusMessage distanceStatus;
	private ComponentController componentController;
	
	@Override
	public void executeCommand(CommandMessage commandMessage) throws Exception {
		if (commandMessage.getCommand() == Command.GET_DISTANCE) {
			Random randomGenerator = new Random();
			distanceStatus = new DistanceStatusMessage();
			distanceStatus.setMeters(randomGenerator.nextDouble()*4);
			componentController.send(distanceStatus);
		}
	}

	public void init() {
		
	}

}
