package com.akibot.tanktrack.component.toggle;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class ToggleComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ToggleComponent.class);

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
