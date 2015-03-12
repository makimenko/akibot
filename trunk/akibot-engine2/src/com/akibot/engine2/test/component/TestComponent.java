package com.akibot.engine2.test.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.network.OutgoingMessageManager;

public class TestComponent extends DefaultComponent {
	private static final AkiLogger log = AkiLogger.create(TestComponent.class);
	private TestResponse lastTestResponse;

	public TestResponse getLastTestResponse() {
		return lastTestResponse;
	}

	@Override
	public void onMessageReceived(Message message) throws FailedToSendMessageException {
		log.trace(this + ": onMessageReceived: " + message);
		if (message instanceof TestRequest) {
			TestRequest request = (TestRequest) message;
			TestResponse response = new TestResponse();
			response.setResult(request.getX() + 1);
			response.copySyncId(request);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		} else if (message instanceof TestResponse) {
			lastTestResponse = (TestResponse) message;
		}
	}

}
