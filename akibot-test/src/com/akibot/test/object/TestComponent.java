package com.akibot.test.object;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class TestComponent extends DefaultComponent {
	private TestResponse lastResponse;
	private int sumOfResponses = 0;
	
	public TestResponse getLastResponse() {
		return lastResponse;
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.copySyncId(message);
			response.setResult(request.getX() + 1);
			lastResponse = response;
			getClient().send(response);
		} else if (message instanceof TestResponse) {
			TestResponse response = (TestResponse)message;
			sumOfResponses += response.getResult();
		}
	}

	public int getSumOfResponses() {
		return sumOfResponses;
	}

	public void setLastResponse(TestResponse lastResponse) {
		this.lastResponse = lastResponse;
	}

}