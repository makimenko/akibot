package com.akibot.engine2.test.component;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class TestComponent extends DefaultComponent {
	private static final AkiLogger log = AkiLogger.create(TestComponent.class);
	private TestResponse lastTestResponse;
	private int[] array;

	public TestComponent() {

	}

	public TestResponse getLastTestResponse() {
		return lastTestResponse;
	}

	@Override
	public void onMessageReceived(Message message) throws FailedToSendMessageException, InterruptedException {
		log.trace(this.getAkibotClient() + ": onMessageReceived: " + message);
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setResult(request.getX() + 1);
			response.copySyncId(request);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		} else if (message instanceof TestSleepRequest) {
			TestSleepRequest testSleepRequest = (TestSleepRequest) message;

			Thread.sleep(testSleepRequest.getSleepMills());
			if (testSleepRequest.getSleepMills() <= 500) {
				TestResponse response = new TestResponse();
				response.copySyncId(message);
				response.setResult((int) testSleepRequest.getSleepMills());
				getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
			} else {
				TestResponse2 response = new TestResponse2();
				response.copySyncId(message);
				response.setResult((int) testSleepRequest.getSleepMills());
				getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
			}

		} else if (message instanceof TestResponse) {
			lastTestResponse = (TestResponse) message;
			if (array != null) {
				array[lastTestResponse.getResult() - 1] = 1;
			}
		}
	}

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

}
