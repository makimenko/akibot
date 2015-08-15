package com.akibot.engine2.component.status;

import java.util.HashMap;
import java.util.TimerTask;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.NooneInterestedException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.monitoring.StatusRequest;
import com.akibot.engine2.network.ClientDescription;

public class StatusWatchdogTimerTask extends TimerTask {
	static final AkiLogger log = AkiLogger.create(StatusWatchdogTimerTask.class);
	private StatusWatchdogComponent statusWatchdogComponent;

	public StatusWatchdogTimerTask(StatusWatchdogComponent statusWatchdogComponent) {
		this.statusWatchdogComponent = statusWatchdogComponent;
	}

	@Override
	public void run() {
		log.trace("StatusWatchdogTimerTask.run()");
		updateList();
		StatusRequest statusRequest = new StatusRequest();
		try {
			statusWatchdogComponent.broadcastMessage(statusRequest);
		} catch (NooneInterestedException e) {
			log.debug("no clients yet");
		} catch (FailedToSendMessageException e) {
			log.catching(statusWatchdogComponent.getAkibotClient(), e);
		}
	}

	private void updateList() {
		// Update my status
		statusWatchdogComponent.getStatusList().put(statusWatchdogComponent.getAkibotClient().getName(), statusWatchdogComponent.getComponentStatus());
		// Update client list
		for (ClientDescription descr : statusWatchdogComponent.getAkibotClient().getClientDescriptionList()) {
			String name = descr.getName();
			if (!statusWatchdogComponent.getStatusList().containsKey(name)) {
				HashMap<String, ComponentStatus> map = statusWatchdogComponent.getStatusList();
				map.put(name, new ComponentStatus());
				statusWatchdogComponent.setStatusList(map);
			}
		}
	}

}
