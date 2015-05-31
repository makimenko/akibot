package com.akibot.jme3.component.visualizer;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.jme3.component.message.VisualizerRequest;

public class VisualizerComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(VisualizerComponent.class);

	private VisualizerWindow visualizerWindow;

	public VisualizerComponent(VisualizerWindow visualizerWindow) {
		this.visualizerWindow = visualizerWindow;
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof VisualizerRequest) {
			visualizerWindow.addQueue((VisualizerRequest) message);
		}

	}

}
