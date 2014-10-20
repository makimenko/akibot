package com.akibot.kiss.tests.messages;

import com.akibot.kiss.component.DefaultComponent;
import com.akibot.kiss.message.Message;

public class TestComponent extends DefaultComponent {
	private TestResponse lastResponse;

	public TestResponse getLastResponse() {
		return lastResponse;
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			if (request.getSyncId() != null) {
				response.setSyncId(request.getSyncId());
			}
			response.setResult(request.getX() + 1);
			lastResponse = response;
			getClient().send(response);
		}
	}

	public void setLastResponse(TestResponse lastResponse) {
		this.lastResponse = lastResponse;
	}

}