package com.akibot.tanktrack.component.distance;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class DistanceMeterComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(DistanceMeterComponent.class);
	private AkibotJniLibrary lib;
	private DistanceMeterConfiguration componentConfiguration;

	@Override
	public void loadDefaults() {
		addTopic(new DistanceRequest());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof DistanceRequest) {
			onDistanceRequest((DistanceRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onDistanceRequest(DistanceRequest distanceRequest) throws FailedToSendMessageException {
		long startTime = System.currentTimeMillis();

		double mm = lib.getDistance(componentConfiguration.getTriggerPin(), componentConfiguration.getEchoPin(),
				(int) componentConfiguration.getTimeoutMicroseconds());

		DistanceResponse response = new DistanceResponse();
		response.setDistanceDetails(new DistanceDetails(mm, mm <= getComponentConfiguration().getMaxDistanceMm()));

		log.trace(this.getAkibotClient() + ": Duration: " + (System.currentTimeMillis() - startTime));
		broadcastResponse(response, distanceRequest);
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		try {
			getComponentStatus().setReady(false);
			componentConfiguration = (DistanceMeterConfiguration) getConfigurationResponse.getComponentConfiguration();
			this.lib = new AkibotJniLibrary();
			this.lib.initialize();
			getComponentStatus().setReady(true);
		} catch (Exception e) {
			throw new FailedToConfigureException(e);
		}
	}

	@Override
	public DistanceMeterConfiguration getComponentConfiguration() {
		return componentConfiguration;
	}

}
