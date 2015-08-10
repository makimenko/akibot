package com.akibot.web.component;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.status.StatusWatchdogIndividualResponse;
import com.akibot.engine2.component.status.StatusWatchdogSummaryResponse;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class AkibotWebComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotWebComponent.class);

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StatusWatchdogIndividualResponse || message instanceof StatusWatchdogSummaryResponse) {
			// Nothing. We use sync request/response
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new StatusWatchdogIndividualResponse());
		addTopic(new StatusWatchdogSummaryResponse());		
		getComponentStatus().setReady(true);
	}

}
