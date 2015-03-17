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
				log.debug(this.getAkibotClient() + ": FORWARD");
				break;
			case BACKWARD:
				log.debug(this.getAkibotClient() + ": BACKWARD");
				break;
			case LEFT:
				log.debug(this.getAkibotClient() + ": LEFT");
				break;
			case RIGHT:
				log.debug(this.getAkibotClient() + ": RIGHT");
				break;
			default:
				log.debug(this.getAkibotClient() + ": STOP");
				break;
			}
		}
	}

	@Override
	public void start() {

	}

}
