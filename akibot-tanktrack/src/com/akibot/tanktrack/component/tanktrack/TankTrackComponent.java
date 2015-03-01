package com.akibot.tanktrack.component.tanktrack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.message.Message;

public class TankTrackComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(TankTrackComponent.class.getName());

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			StickMotionRequest request = (StickMotionRequest) message;

			switch (request.getDirectionType()) {
			case FORWARD:
				log.debug("FORWARD");
				break;
			case BACKWARD:
				log.debug("BACKWARD");
				break;
			case LEFT:
				log.debug("LEFT");
				break;
			case RIGHT:
				log.debug("RIGHT");
				break;
			default:
				log.debug("STOP");
				break;
			}
		}
	}

	@Override
	public void start() {

	}

}
