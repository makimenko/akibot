package com.akibot.tanktrack.component.toggle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.message.Message;

public class ToggleComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(ToggleComponent.class.getName());

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ToggleRequest) {
			ToggleRequest request = (ToggleRequest) message;

			switch (request.getType()) {
			case ON:
				log.debug(this.getAkibotClient() + ": ON");
				break;
			case OFF:
				log.debug(this.getAkibotClient() + ": OFF");
				break;
			default:
				log.debug(this.getAkibotClient() + ": UNKNOWN COMMAND: " + request.getType());
				break;
			}
		}
	}

}
