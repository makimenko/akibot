package com.akibot.engine2.component.configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class ConfigurationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ConfigurationComponent.class);
	private String dir = ".";
	private final String FILE_NAME_REGEX_EXCLUDE = "[^\\w\\.\\-+]";
	private final String FILE_EXTENSION = ".akiconfig";

	public ConfigurationComponent(String dir) {
		this.dir = dir;
	}

	@Override
	public void loadDefaults() {
		addTopic(new GetConfigurationRequest());
		addTopic(new PutConfigurationRequest());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof GetConfigurationRequest) {
			onGetConfigurationRequest((GetConfigurationRequest) message);
		} else if (message instanceof PutConfigurationRequest) {
			onPutConfigurationRequest((PutConfigurationRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onGetConfigurationRequest(GetConfigurationRequest getConfigurationRequest) throws FailedToSendMessageException, ClassNotFoundException,
			IOException {
		log.debug(this.getAkibotClient() + ": " + getConfigurationRequest);
		GetConfigurationResponse getConfigurationResponse = new GetConfigurationResponse();
		getConfigurationResponse.setComponentConfiguration(loadFromFile(nameToFileName(getConfigurationRequest.getName())));
		getConfigurationResponse.setTo(getConfigurationRequest.getFrom());
		broadcastResponse(getConfigurationResponse, getConfigurationRequest);
	}

	private void onPutConfigurationRequest(PutConfigurationRequest putConfigurationRequest) throws FailedToSendMessageException, IOException {
		log.debug(this.getAkibotClient() + ": " + putConfigurationRequest);
		PutConfigurationResponse putConfigurationResponse = new PutConfigurationResponse();
		saveToFile(nameToFileName(putConfigurationRequest.getName()), putConfigurationRequest.getComponentConfiguration());
		broadcastResponse(putConfigurationResponse, putConfigurationRequest);
	}

	private void saveToFile(String fileName, ComponentConfiguration value) throws IOException {
		log.debug(getAkibotClient() + ": Save configuration to file: " + fileName);
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(value);
		oos.close();
	}

	private ComponentConfiguration loadFromFile(String fileName) throws IOException, ClassNotFoundException {
		log.debug(getAkibotClient() + ": Load configuration from file: " + fileName);
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Serializable value = (Serializable) ois.readObject();
		ois.close();
		return (ComponentConfiguration) value;
	}

	private String nameToFileName(String name) {
		return dir + "/" + name.replaceAll(FILE_NAME_REGEX_EXCLUDE, "_") + FILE_EXTENSION;
	}

	@Override
	public void startComponent() throws FailedToStartException {
		super.getComponentStatus().setReady(true);
	}
}