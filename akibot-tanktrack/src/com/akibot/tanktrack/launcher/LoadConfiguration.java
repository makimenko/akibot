package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.DefaultServerComponent;
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

	public LoadConfiguration() throws Exception {
		String serverHost = Constants.SERVER_HOST;
		int serverPort = Constants.SERVER_PORT + 1;
		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		AkibotClient server = new AkibotClient("LoadConfiguration.server", new DefaultServerComponent(), serverPort);

		AkibotClient configClient = new AkibotClient("LoadConfiguration.config", new ConfigurationComponent("."), serverAddress);
		configClient.getMyClientDescription().getTopicList().add(new GetConfigurationRequest());
		configClient.getMyClientDescription().getTopicList().add(new PutConfigurationRequest());

		client = new AkibotClient("LoadConfiguration.client", new DefaultComponent(), serverAddress);
		client.getMyClientDescription().getTopicList().add(new GetConfigurationResponse());
		client.getMyClientDescription().getTopicList().add(new PutConfigurationResponse());

		server.start();
		configClient.start();
		client.start();

		Thread.sleep(100);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Initializing...");
		LoadConfiguration loadConfiguration = new LoadConfiguration();

		System.out.println("Upload default configuration...");
		loadConfiguration.setGyroscopeConfiguration();

		System.out.println("Done.");
	}

	private void setGyroscopeConfiguration() throws FailedToSendMessageException {
		GyroscopeOffsetConfiguration gyroscopeConfiguration = new GyroscopeOffsetConfiguration();
		gyroscopeConfiguration.setOffsetDegrees(180);
		gyroscopeConfiguration.setOffsetX(408.0);
		gyroscopeConfiguration.setOffsetY(-330.0);
		gyroscopeConfiguration.setOffsetZ(-1095.5);

		PutConfigurationRequest putConfigurationRequest = new PutConfigurationRequest();
		putConfigurationRequest.setName("akibot.gyroscope-gyroscopeConfiguration");
		putConfigurationRequest.setValue(gyroscopeConfiguration);
		client.getOutgoingMessageManager().broadcastMessage(putConfigurationRequest);
	}

}
