package com.akibot.web.component;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.status.StatusWatchdogResponse;

public class AkibotWebComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotWebComponent.class);

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StatusWatchdogResponse) {
			onStatusWatchdogResponse((StatusWatchdogResponse) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onStatusWatchdogResponse(StatusWatchdogResponse message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadDefaults() {
		addTopic(new StatusWatchdogResponse());
		getComponentStatus().setReady(true);
	}

}
