package com.akibot.web.listener;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.akibot.engine2.component.ComponentStatus;
import com.akibot.engine2.exception.FailedClientConstructorException;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.network.ClientDescription;
import com.akibot.tanktrack.component.status.StatusWatchdogRequest;
import com.akibot.tanktrack.component.status.StatusWatchdogResponse;
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

	public static List<SimplifiedClientDescription> getSimplifiedClientDescriptionList() {
		List<ClientDescription> list = akibotWebComponent.getAkibotClient().getClientDescriptionList();

		List<SimplifiedClientDescription> result = new ArrayList<SimplifiedClientDescription>();
		for (ClientDescription descr : list) {
			StatusWatchdogRequest statusWatchdogRequest = new StatusWatchdogRequest();
			statusWatchdogRequest.setComponentName(descr.getName());
			ComponentStatus componentStatus;
			try {
				StatusWatchdogResponse statusWatchdogResponse = (StatusWatchdogResponse) akibotWebComponent.sendSyncRequest(statusWatchdogRequest, 100);
				componentStatus = statusWatchdogResponse.getComponentStatus();
			} catch (FailedToSendMessageException e) {
				componentStatus = null;
			}

			result.add(BeanUtils.simplifyClientDescription(descr, componentStatus));
		}
		return result;
	}

}
