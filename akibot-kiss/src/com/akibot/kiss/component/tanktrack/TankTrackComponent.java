package com.akibot.kiss.component.tanktrack;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.request.StickMotionRequest;

public class TankTrackComponent extends DefaultComponent {

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			StickMotionRequest request = (StickMotionRequest) message;

			switch (request.getDirectionType()) {
			case FORWARD:
				System.out.println("FORWARD");
				break;
			case BACKWARD:
				System.out.println("BACKWARD");
				break;
			case LEFT:
				System.out.println("LEFT");
				break;
			case RIGHT:
				System.out.println("RIGHT");
				break;
			default:
				System.out.println("STOP");
				break;
			}
		}
	}

}
