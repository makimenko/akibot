package com.akibot.kiss.component.awtcontroller;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Message;
import com.akibot.kiss.message.response.DistanceResponse;

public class AwtControllerComponent extends DefaultComponent {
	AwtControllerAppl appl;

	public AwtControllerComponent() {

	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof DistanceResponse) {
			DistanceResponse distanceResponse = (DistanceResponse) message;
			appl.getTextArea().append("Distance is: " + distanceResponse.getMeters() + " meters\n");
		}
		super.processMessage(message);
	}

	public void start() {
		this.appl = new AwtControllerAppl(this.getClient());
		appl.start();
	}

}
