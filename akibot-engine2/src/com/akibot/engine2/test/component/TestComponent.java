package com.akibot.engine2.test.component;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.message.Message;

public class TestComponent extends DefaultComponent {

	public TestComponent(String name) {
		super(name);
	}

	@Override
	public void onMessageReceived(Message message) {
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setResult(request.getX() + 1);
		}
	}

}
