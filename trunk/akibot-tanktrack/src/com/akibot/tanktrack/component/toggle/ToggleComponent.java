package com.akibot.tanktrack.component.toggle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class ToggleComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(ToggleComponent.class.getName());

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof ToggleRequest) {
			ToggleRequest request = (ToggleRequest) message;

			switch (request.getType()) {
			case ON:
				log.debug("ON");
				break;
			case OFF:
				log.debug("OFF");
				break;
			default:
				log.debug("UNKNOWN COMMAND: " + request.getType());
				break;
			}
		}
	}

}
