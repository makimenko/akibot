package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class TankTrackComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(TankTrackComponent.class);

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
