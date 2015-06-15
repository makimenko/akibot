package com.akibot.tanktrack.component.audio;

import com.akibot.engine2.message.Request;

public class AudioRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String audioUrl;

	public AudioRequest() {

	}

	public AudioRequest(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	@Override
	public String toString() {
		return "AudioRequest: " + audioUrl;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

}
