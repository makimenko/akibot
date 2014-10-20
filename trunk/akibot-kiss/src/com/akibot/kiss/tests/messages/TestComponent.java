package com.akibot.kiss.tests.messages;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Message;

public class TestComponent extends DefaultComponent {
	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setSyncId(request.getSyncId()); // TODO: how to do it
														// automatically?
			response.setResult(request.getX() + 1);
			getClient().send(response);
		}
	}
}