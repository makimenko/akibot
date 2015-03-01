package com.akibot.tanktrack.component.awtcontroller;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;

public class AwtControllerComponent extends DefaultComponent {
	AwtControllerAppl appl;

	public AwtControllerComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof Response) {
			appl.getTextArea().append(message + "\n");
		}
		super.onMessageReceived(message);
	}

	@Override
	public void start() {
		this.appl = new AwtControllerAppl(getAkibotClient());
		appl.start();
	}

}
