package com.akibot.engine2.test.component;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class TestComponent extends DefaultComponent {
	private static final AkiLogger log = AkiLogger.create(TestComponent.class);
	private TestResponse lastTestResponse;
	private int[] array;

	public TestResponse getLastTestResponse() {
		return lastTestResponse;
	}

	@Override
	public void loadDefaults() {
		super.getComponentStatus().setReady(true);
	}

	@Override
	public void onMessageReceived(Message message) throws FailedToSendMessageException, InterruptedException, UnsupportedMessageException {
		if (message instanceof TestRequest) {
			onTestRequest((TestRequest) message);
		} else if (message instanceof TestSleepRequest) {
			onTestSleepRequest((TestSleepRequest) message);
		} else if (message instanceof TestResponse) {
			onTestResponse((TestResponse) message);
		} else {
			// throw new UnsupportedMessageException(message.toString());
			// Nothing, because it's test component with all messages
		}
	}

	private void onTestRequest(TestRequest testRequest) throws FailedToSendMessageException {
		TestResponse response = new TestResponse();
		response.setResult(testRequest.getX() + 1);
		broadcastResponse(response, testRequest);
	}

	private void onTestSleepRequest(TestSleepRequest testSleepRequest) throws InterruptedException, FailedToSendMessageException {
		Thread.sleep(testSleepRequest.getSleepMills());
		if (testSleepRequest.getSleepMills() <= 500) {
			TestResponse response = new TestResponse();
			response.setResult((int) testSleepRequest.getSleepMills());
			broadcastResponse(response, testSleepRequest);
		} else {
			TestResponse2 response = new TestResponse2();
			response.setResult((int) testSleepRequest.getSleepMills());
			broadcastResponse(response, testSleepRequest);
		}
	}

	private void onTestResponse(TestResponse testResponse) {
		lastTestResponse = testResponse;
		if (array != null) {
			array[lastTestResponse.getResult() - 1] = 1;
		}
	}

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

}
