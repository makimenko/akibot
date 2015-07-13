package com.akibot.engine2.component.configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class ConfigurationComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ConfigurationComponent.class);

	private final String FILE_EXTENSION = ".akiconfig";

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
		getConfigurationResponse.setValue(loadFromFile(nameToFileName(getConfigurationRequest.getName())));
		broadcastResponse(getConfigurationResponse, getConfigurationRequest);
	}

	private void onPutConfigurationRequest(PutConfigurationRequest putConfigurationRequest) throws FailedToSendMessageException, IOException {
		log.debug(this.getAkibotClient() + ": " + putConfigurationRequest);
		PutConfigurationResponse putConfigurationResponse = new PutConfigurationResponse();
		saveToFile(nameToFileName(putConfigurationRequest.getName()), putConfigurationRequest.getValue());
		broadcastResponse(putConfigurationResponse, putConfigurationRequest);
	}

	private void saveToFile(String fileName, Serializable value) throws IOException {
		log.debug(getAkibotClient() + ": Save configuration to file: " + fileName);
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(value);
		oos.close();
	}

	private Serializable loadFromFile(String fileName) throws IOException, ClassNotFoundException {
		log.debug(getAkibotClient() + ": Load configuration from file: " + fileName);
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Serializable value = (Serializable) ois.readObject();
		ois.close();
		return value;
	}

	private String nameToFileName(String name) {
		return name + FILE_EXTENSION;
	}
}
