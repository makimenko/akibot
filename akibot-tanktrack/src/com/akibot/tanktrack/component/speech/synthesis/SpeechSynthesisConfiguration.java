package com.akibot.tanktrack.component.speech.synthesis;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class SpeechSynthesisConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;
	private String marytssDefaultVoice;
	private String maryttsHost;
	private int maryttsPort;

	public String getMarytssDefaultVoice() {
		return marytssDefaultVoice;
	}

	public void setMarytssDefaultVoice(String marytssDefaultVoice) {
		this.marytssDefaultVoice = marytssDefaultVoice;
	}

	public String getMaryttsHost() {
		return maryttsHost;
	}

	public void setMaryttsHost(String maryttsHost) {
		this.maryttsHost = maryttsHost;
	}

	public int getMaryttsPort() {
		return maryttsPort;
	}

	public void setMaryttsPort(int maryttsPort) {
		this.maryttsPort = maryttsPort;
	}

}
