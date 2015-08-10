package com.akibot.web.listener;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.component.status.StatusWatchdogIndividualRequest;
import com.akibot.engine2.component.status.StatusWatchdogIndividualResponse;
import com.akibot.engine2.component.status.StatusWatchdogSummaryRequest;
import com.akibot.engine2.component.status.StatusWatchdogSummaryResponse;
import com.akibot.engine2.exception.FailedClientConstructorException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.tanktrack.launcher.Constants;
import com.akibot.web.bean.BeanUtils;
import com.akibot.web.bean.SimplifiedClientDescription;
import com.akibot.web.component.AkibotWebComponent;

public class AkiBotWebMaster {
	private static AkibotWebComponent akibotWebComponent;
	private static boolean initialized;

	static {
		initialized = false;
		String dnsHost = Constants.DNS_WEBTEST_HOST;
		int dnsPort = Constants.DNS_WEBTEST_PORT;

		try {
			InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);
			AkiBotWebMaster.akibotWebComponent = new AkibotWebComponent();
			AkibotClient akibotWebClient = new AkibotClient("akibot.web", AkiBotWebMaster.akibotWebComponent, dnsAddress);
			akibotWebClient.start();
			initialized = true;
			System.out.println("** AkibotWebListener: initialized");
		} catch (FailedClientConstructorException e) {
			System.out.println("** AkibotWebListener: failed");
			e.printStackTrace();
		}
	}

	public static void init() {

	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static AkibotWebComponent getAkibotWebComponent() {
		return akibotWebComponent;
	}

	public static List<SimplifiedClientDescription> getSimplifiedClientDescriptionList() throws FailedToSendMessageException {
		List<ClientDescription> list = akibotWebComponent.getAkibotClient().getClientDescriptionList();

		StatusWatchdogSummaryRequest statusWatchdogSummaryRequest = new StatusWatchdogSummaryRequest();
		StatusWatchdogSummaryResponse statusWatchdogSummaryResponse = (StatusWatchdogSummaryResponse) akibotWebComponent.sendSyncRequest(
				statusWatchdogSummaryRequest, 100);

		List<SimplifiedClientDescription> result = new ArrayList<SimplifiedClientDescription>();
		// add myself:
		result.add(BeanUtils.simplifyClientDescription(akibotWebComponent.getAkibotClient().getMyClientDescription(), statusWatchdogSummaryResponse
				.getSummaryMap().get(akibotWebComponent.getAkibotClient().getName())));
		// add my clients:
		for (ClientDescription descr : list) {
			result.add(BeanUtils.simplifyClientDescription(descr, statusWatchdogSummaryResponse.getSummaryMap().get(descr.getName())));
		}
		return result;
	}

}
