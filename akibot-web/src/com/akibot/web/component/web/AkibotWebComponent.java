package com.akibot.web.component.web;

import org.json.JSONObject;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.status.StatusWatchdogIndividualResponse;
import com.akibot.engine2.component.status.StatusWatchdogSummaryResponse;
import com.akibot.engine2.component.test.TestResponse;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationResponse;
import com.akibot.tanktrack.component.orientation.OrientationResponse;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisResponse;
import com.akibot.tanktrack.component.tanktrack.MotionResponse;
import com.akibot.tanktrack.component.world.message.WorldResponse;
import com.akibot.web.listener.AkiBotWebMaster;

public class AkibotWebComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AkibotWebComponent.class);
	private AkiBotWebMaster akiBotWebMaster;

	public AkibotWebComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		log.warn("Response is: " + message);
		JSONObject objectMessage = new JSONObject(message);
		// JSONObject objectMessage = new JSONObject();
		// objectMessage.put("value", message.toString());

		// If at least one client exists, then inform him:
		AkiBotWebMaster.sendToAllWebSocketSessions(objectMessage);
	}

	@Override
	public void loadDefaults() {
		addTopic(new StatusWatchdogIndividualResponse());
		addTopic(new StatusWatchdogSummaryResponse());
		addTopic(new TestResponse());
		addTopic(new OrientationResponse());
		addTopic(new MotionResponse());
		addTopic(new DistanceResponse());
		addTopic(new GyroscopeResponse());
		addTopic(new SpeechSynthesisResponse());
		addTopic(new GyroscopeCalibrationResponse());
		addTopic(new WorldResponse());

		getComponentStatus().setReady(true);
	}

	public AkiBotWebMaster getAkiBotWebMaster() {
		return akiBotWebMaster;
	}

	public void setAkiBotWebMaster(AkiBotWebMaster akiBotWebMaster) {
		this.akiBotWebMaster = akiBotWebMaster;
	}

}
