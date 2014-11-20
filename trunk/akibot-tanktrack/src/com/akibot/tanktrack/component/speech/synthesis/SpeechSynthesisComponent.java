package com.akibot.tanktrack.component.speech.synthesis;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.util.data.audio.AudioPlayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class SpeechSynthesisComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(SpeechSynthesisComponent.class.getName());
	private LineListener lineListener;
	private String marytssVoice;
	private MaryInterface marytts;
	private String maryttsHost;
	private int maryttsPort;

	public SpeechSynthesisComponent(String maryttsHost, int maryttsPort, String marytssVoice) {
		this.maryttsHost = maryttsHost;
		this.maryttsPort = maryttsPort;
		this.marytssVoice = marytssVoice;
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof SpeechSynthesisRequest) {
			SpeechSynthesisRequest request = (SpeechSynthesisRequest) message;
			log.debug("Speech request: " + request);

			if (request.getSpeechText() != null && request.getSpeechText().length() > 0) {
				AudioInputStream audio = marytts.generateAudio(request.getSpeechText());
				AudioPlayer player = new AudioPlayer(audio, lineListener);
				player.start();
				player.join();
			}
		}
	}

	@Override
	public void start() throws Exception {
		marytts = new RemoteMaryInterface(maryttsHost, maryttsPort);
		marytts.setVoice(marytssVoice);

		lineListener = new LineListener() {
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.START) {
					log.trace("Audio started playing");
				} else if (event.getType() == LineEvent.Type.STOP) {
					log.trace("Audio stopped playing");
				} else if (event.getType() == LineEvent.Type.OPEN) {
					log.trace("Audio line opened");
				} else if (event.getType() == LineEvent.Type.CLOSE) {
					log.trace("Audio line closed");
				}
			}
		};

	}

}
