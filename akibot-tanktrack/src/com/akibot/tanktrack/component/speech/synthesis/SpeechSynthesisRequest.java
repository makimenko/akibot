package com.akibot.tanktrack.component.speech.synthesis;

import com.akibot.engine2.message.Request;

public class SpeechSynthesisRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String speechText;
	private String voice;

	public SpeechSynthesisRequest(String speechText) {
		this.speechText = speechText;
	}

	public SpeechSynthesisRequest() {
	}

	public String getSpeechText() {
		return speechText;
	}

	public void setSpeechText(String speechText) {
		this.speechText = speechText;
	}

	@Override
	public String toString() {
		return "SpeechSynthesisRequest (" + voice + "): " + speechText;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

}
