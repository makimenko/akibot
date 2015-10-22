package com.akibot.engine2.component.workflow;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

/**
 * 
 * Element: - Start - End - Request - SyncRequest - Decission - Fork - Join
 * 
 * Events: - onError - onMessage
 * 
 * Workflow: - id
 * 
 *
 */

public class WorkflowComponent extends DefaultComponent {
	private static final AkiLogger log = AkiLogger.create(WorkflowComponent.class);

	@Override
	public void loadDefaults() {
		super.getComponentStatus().setReady(true);
	}

	@Override
	public void onMessageReceived(Message message) throws FailedToSendMessageException, InterruptedException, UnsupportedMessageException {

	}

}
