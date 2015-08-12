package com.akibot.web.component;

import org.json.JSONObject;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.status.StatusWatchdogIndividualResponse;
import com.akibot.engine2.component.status.StatusWatchdogSummaryResponse;
import com.akibot.engine2.component.test.TestResponse;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.web.listener.AkiBotWebMaster;

public class AkibotWebComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotWebComponent.class);
	private AkiBotWebMaster akiBotWebMaster;

	public AkibotWebComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StatusWatchdogIndividualResponse || message instanceof StatusWatchdogSummaryResponse) {
			log.trace("AkibotWebComponent.onMessageReceived: " + message);
		} else if (message instanceof TestResponse) {
			JSONObject objectMessage = new JSONObject(message);
			// JSONObject objectMessage = new JSONObject();
			// objectMessage.put("value", message.toString());
			AkiBotWebMaster.getMySessionHandler().sendToAllConnectedSessions(objectMessage);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new StatusWatchdogIndividualResponse());
		addTopic(new StatusWatchdogSummaryResponse());
		addTopic(new TestResponse());
		getComponentStatus().setReady(true);
	}

	public AkiBotWebMaster getAkiBotWebMaster() {
		return akiBotWebMaster;
	}

	public void setAkiBotWebMaster(AkiBotWebMaster akiBotWebMaster) {
		this.akiBotWebMaster = akiBotWebMaster;
	}

}
