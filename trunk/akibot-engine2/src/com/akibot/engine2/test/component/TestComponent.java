package com.akibot.engine2.test.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.message.Message;

public class TestComponent extends DefaultComponent {
	private static final Logger log = LogManager.getLogger(TestComponent.class.getName());

	public TestComponent(String name) {
		super(name);
	}

	@Override
	public void onMessageReceived(Message message) throws FailedToSendMessageException {
		log.trace(this + ": onMessageReceived: " + message);
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setResult(request.getX() + 1);
			response.copySyncId(request);
			broadcastMessage(response);
		}
	}

}
