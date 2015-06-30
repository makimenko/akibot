package com.akibot.jme3.component.visualizer.world;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.jme3.component.message.VisualizerRequest;

public class WorldComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(WorldComponent.class);

	public WorldComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof VisualizerRequest) {

		}

	}

}
