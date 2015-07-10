package com.akibot.tanktrack.component.awtcontroller;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;

public class AwtControllerComponent extends DefaultComponent {
	AwtControllerAppl appl;

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof Response) {
			onResponse((Response) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onResponse(Response response) {
		appl.getTextArea().append(response + "\n");
	}

	@Override
	public void start() {
		this.appl = new AwtControllerAppl(getAkibotClient());
		appl.start();
	}

}
