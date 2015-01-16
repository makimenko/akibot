package com.akibot.tanktrack.component.awtcontroller;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;
import com.akibot.engine.message.Response;

public class AwtControllerComponent extends DefaultComponent {
	AwtControllerAppl appl;

	public AwtControllerComponent() {

	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof Response) {
			appl.getTextArea().append(message + "\n");
		}
		super.processMessage(message);
	}

	@Override
	public void run() {
		this.appl = new AwtControllerAppl(this.getClient());
		appl.start();
	}

}
