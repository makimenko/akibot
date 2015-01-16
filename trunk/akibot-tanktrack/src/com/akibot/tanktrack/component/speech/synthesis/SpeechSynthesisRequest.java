package com.akibot.tanktrack.component.speech.synthesis;

import com.akibot.engine.message.Request;

public class SpeechSynthesisRequest extends Request {
	private static final long serialVersionUID = 1L;
	private String speechText;

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
		return "SpeechSynthesisRequest: " + speechText;
	}

}
