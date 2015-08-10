package com.akibot.engine2.component.status;

import java.util.HashMap;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.message.Response;

public class StatusWatchdogSummaryResponse extends Response {
	private static final long serialVersionUID = 1L;

	private HashMap<String, ComponentStatus> summaryMap;

	public HashMap<String, ComponentStatus> getSummaryMap() {
		return summaryMap;
	}

	public void setSummaryMap(HashMap<String, ComponentStatus> summaryMap) {
		this.summaryMap = summaryMap;
	}

}
