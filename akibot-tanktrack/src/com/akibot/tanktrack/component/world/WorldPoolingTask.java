package com.akibot.tanktrack.component.world;

import java.util.TimerTask;

import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;

public class WorldPoolingTask extends TimerTask {
	static final AkiLogger log = AkiLogger.create(WorldPoolingTask.class);
	private WorldComponent worldComponent;
	private GyroscopeValueRequest gyroscopeValueRequest;

	public WorldPoolingTask(WorldComponent worldComponent) {
		this.worldComponent = worldComponent;
		this.gyroscopeValueRequest = new GyroscopeValueRequest();
		log.debug("WorldPoolingTask initialized");
	}

	public void run() {
		log.debug(worldComponent.getAkibotClient() + ": WorldPoolingTask.run()");
		try {
			gyroscopeValueRequest.setFrom(worldComponent.getAkibotClient().getName()); // private message request/response
			worldComponent.broadcastMessage(gyroscopeValueRequest);
		} catch (FailedToSendMessageException e) {
			log.catching(worldComponent.getAkibotClient(), e);
		}

	}
}
