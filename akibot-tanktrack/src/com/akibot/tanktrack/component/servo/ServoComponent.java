package com.akibot.tanktrack.component.servo;

import java.io.Serializable;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.configuration.ComponentConfiguration;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class ServoComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ServoComponent.class);
	private AkibotJniLibrary lib;
	private ServoConfiguration componentConfiguration;

	@Override
	public ComponentConfiguration getComponentConfiguration() {
		return this.componentConfiguration;
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		Serializable responseValue = getConfigurationResponse.getComponentConfiguration();
		if (responseValue instanceof ServoConfiguration) {
			setComponentConfiguration((ServoConfiguration) responseValue);
		} else {
			throw new FailedToConfigureException(responseValue.toString());
		}
	}

	public void setComponentConfiguration(ServoConfiguration componentConfiguration) throws FailedToConfigureException {
		this.componentConfiguration = componentConfiguration;
		init();
	}

	private void init() throws FailedToConfigureException {
		log.debug(this.getAkibotClient() + ": Initializing Servo...");
		getComponentStatus().setReady(false);
		if (componentConfiguration != null) {
			try {
				this.lib = new AkibotJniLibrary();
				this.lib.initialize();
				getComponentStatus().setReady(true);
			} catch (Exception e) {
				throw new FailedToConfigureException(e);
			}
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new ServoRequest());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ServoRequest) {
			onServoRequest((ServoRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onServoRequest(ServoRequest servoRequest) throws FailedToSendMessageException {
		ServoResponse response = new ServoResponse();
		this.lib.servo(componentConfiguration.getServoPin(), componentConfiguration.getInitialValue(), componentConfiguration.getPwmRange(),
				componentConfiguration.getDivisor(), servoRequest.getValue(), servoRequest.getMicroseconds());
		broadcastResponse(response, servoRequest);
	}

	@Override
	public void startComponent() throws FailedToStartException {

	}

}
