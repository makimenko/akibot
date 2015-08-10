package com.akibot.engine2.component.status;

import java.util.HashMap;
import java.util.Timer;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.monitoring.StatusResponse;

public class StatusWatchdogComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(StatusWatchdogComponent.class);

	private HashMap<String, ComponentStatus> statusList = new HashMap<String, ComponentStatus>();
	private Timer timer;

	public StatusWatchdogComponent(long delay, long period) {
		timer = new Timer();
		timer.schedule(new StatusWatchdogTimerTask(this), delay, period);
	}

	public HashMap<String, ComponentStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(HashMap<String, ComponentStatus> statusList) {
		this.statusList = statusList;
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StatusWatchdogIndividualRequest) {
			onStatusWatchdogIndividualRequest((StatusWatchdogIndividualRequest) message);
		} else if (message instanceof StatusWatchdogSummaryRequest) {
			onStatusWatchdogSummaryRequest((StatusWatchdogSummaryRequest) message);
		} else if (message instanceof StatusResponse) {
			onStatusResponse((StatusResponse) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new StatusWatchdogIndividualRequest());
		addTopic(new StatusWatchdogSummaryRequest());
		addTopic(new StatusResponse());
		getComponentStatus().setReady(true);
	}

	private void onStatusWatchdogIndividualRequest(StatusWatchdogIndividualRequest statusWatchdogIndividualRequest) throws FailedToSendMessageException {
		log.debug(this.getAkibotClient() + ": " + statusWatchdogIndividualRequest);
		StatusWatchdogIndividualResponse response = new StatusWatchdogIndividualResponse();

		ComponentStatus componentStatus = new ComponentStatus();
		componentStatus = statusList.get(statusWatchdogIndividualRequest.getComponentName());
		response.setComponentStatus(componentStatus);

		broadcastResponse(response, statusWatchdogIndividualRequest);
	}

	private void onStatusWatchdogSummaryRequest(StatusWatchdogSummaryRequest statusWatchdogSummaryRequest) throws FailedToSendMessageException {
		log.debug(this.getAkibotClient() + ": " + statusWatchdogSummaryRequest);
		StatusWatchdogSummaryResponse response = new StatusWatchdogSummaryResponse();
		response.setSummaryMap(getStatusList());

		broadcastResponse(response, statusWatchdogSummaryRequest);
	}

	private void onStatusResponse(StatusResponse statusResponse) {
		String from = statusResponse.getFrom();
		ComponentStatus componentStatus = statusResponse.getComponentStatus();
		statusList.put(from, componentStatus);
	}

}
