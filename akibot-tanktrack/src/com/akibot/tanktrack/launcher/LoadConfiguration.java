package com.akibot.tanktrack.launcher;

import java.io.Serializable;
import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.component.configuration.GetConfigurationRequest;
import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.component.configuration.PutConfigurationRequest;
import com.akibot.engine2.component.configuration.PutConfigurationResponse;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.gyroscope.GyroscopeOffsetConfiguration;

public class LoadConfiguration {
	static final AkiLogger log = AkiLogger.create(LoadConfiguration.class);
	private AkibotClient client;
	private int TIMEOUT = 1000;

	public LoadConfiguration() throws Exception {
		String dnsHost = Constants.DNS_HOST;
		int dnsPort = Constants.DNS_PORT + 1;
		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		AkibotClient dns = new AkibotClient("LoadConfiguration.dns", new DefaultDNSComponent(), dnsPort);

		AkibotClient configClient = new AkibotClient("LoadConfiguration.config", new ConfigurationComponent("."), dnsAddress);
		configClient.getMyClientDescription().getTopicList().add(new GetConfigurationRequest());
		configClient.getMyClientDescription().getTopicList().add(new PutConfigurationRequest());

		client = new AkibotClient("LoadConfiguration.client", new DefaultComponent(), dnsAddress);
		client.getMyClientDescription().getTopicList().add(new GetConfigurationResponse());
		client.getMyClientDescription().getTopicList().add(new PutConfigurationResponse());

		dns.start();
		configClient.start();
		client.start();

		Thread.sleep(100);
	}

	public static void main(String[] args) throws Exception {
		LoadConfiguration loadConfiguration = new LoadConfiguration();

		System.out.println("Upload default configuration:");
		loadConfiguration.setGyroscopeConfiguration();

		System.out.println("Done");
	}

	private void setGyroscopeConfiguration() throws FailedToSendMessageException {
		GyroscopeOffsetConfiguration gyroscopeConfiguration = new GyroscopeOffsetConfiguration();
		gyroscopeConfiguration.setOffsetDegrees(180);
		gyroscopeConfiguration.setOffsetX(466.5);
		gyroscopeConfiguration.setOffsetY(-256.5);
		gyroscopeConfiguration.setOffsetZ(-1091.0);

		save("akibot.gyroscope-gyroscopeConfiguration", gyroscopeConfiguration);
	}

	private void save(String name, Serializable value) throws FailedToSendMessageException {
		System.out.println("  Save: " + name);
		PutConfigurationRequest putConfigurationRequest = new PutConfigurationRequest();
		putConfigurationRequest.setName(name);
		putConfigurationRequest.setValue(value);
		client.getOutgoingMessageManager().sendSyncRequest(putConfigurationRequest, TIMEOUT);
	}

}
