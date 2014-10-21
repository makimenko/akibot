package com.akibot.tanktrack.component.orientation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class OrientationComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(OrientationComponent.class.getName());

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof OrientationRequest) {

		}
	}

}
