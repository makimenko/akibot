package com.akibot.tanktrack.component.speech.synthesis;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class SpeechSynthesisComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(SpeechSynthesisComponent.class);
	private LineListener lineListener;
	private String marytssDefaultVoice;
	private MaryInterface marytts;
	private String maryttsHost;
	private int maryttsPort;
	private String lastSpeech;

	public SpeechSynthesisComponent(String maryttsHost, int maryttsPort, String marytssDefaultVoice) {
		this.maryttsHost = maryttsHost;
		this.maryttsPort = maryttsPort;
		this.marytssDefaultVoice = marytssDefaultVoice;
	}

	@Override
	public void loadDefaultTopicList() {
		addTopic(new SpeechSynthesisRequest());
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof SpeechSynthesisRequest) {
			onSpeechSynthesisRequest((SpeechSynthesisRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onSpeechSynthesisRequest(SpeechSynthesisRequest speechSynthesisRequest) throws SynthesisException, FailedToSendMessageException,
			InterruptedException {
		SpeechSynthesisResponse response = new SpeechSynthesisResponse();

		log.debug(this.getAkibotClient() + ": Speech request: " + speechSynthesisRequest);
		marytts.setVoice(marytssDefaultVoice);

		if (speechSynthesisRequest.getVoice() != null && speechSynthesisRequest.getVoice().length() > 0
				&& !marytts.getVoice().equals(speechSynthesisRequest.getVoice())) {
			marytts.setVoice(speechSynthesisRequest.getVoice());
		}
		log.debug(this.getAkibotClient() + " Voice: " + marytts.getVoice());

		if (speechSynthesisRequest.getSpeechText() != null && speechSynthesisRequest.getSpeechText().length() > 0) {
			AudioInputStream audio = marytts.generateAudio(speechSynthesisRequest.getSpeechText());
			lastSpeech = speechSynthesisRequest.getSpeechText();
			AudioPlayer player = new AudioPlayer(audio, lineListener);
			player.start();
			player.join();
		}

		broadcastResponse(response, speechSynthesisRequest);
	}

	@Override
	public void startComponent() throws FailedToStartException {
		try {
			marytts = new RemoteMaryInterface(maryttsHost, maryttsPort);
			marytts.setVoice(marytssDefaultVoice);
			lineListener = new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.START) {
						log.trace("Speech Started: " + lastSpeech);
					} else if (event.getType() == LineEvent.Type.STOP) {
						log.trace("Speech Stoped: " + lastSpeech);
					} else if (event.getType() == LineEvent.Type.OPEN) {
						log.trace("Audio line opened");
					} else if (event.getType() == LineEvent.Type.CLOSE) {
						log.trace("Audio line closed");
					}
				}
			};
		} catch (IOException e) {
			throw new FailedToStartException(e);
		}

	}

}
