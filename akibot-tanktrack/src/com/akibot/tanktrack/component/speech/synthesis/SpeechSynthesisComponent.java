package com.akibot.tanktrack.component.speech.synthesis;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.util.data.audio.AudioPlayer;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class SpeechSynthesisComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(SpeechSynthesisComponent.class);
	private LineListener lineListener;
	private String marytssDefaultVoice;
	private MaryInterface marytts;
	private String maryttsHost;
	private int maryttsPort;

	public SpeechSynthesisComponent(String maryttsHost, int maryttsPort, String marytssDefaultVoice) {
		this.maryttsHost = maryttsHost;
		this.maryttsPort = maryttsPort;
		this.marytssDefaultVoice = marytssDefaultVoice;
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof SpeechSynthesisRequest) {
			SpeechSynthesisRequest request = (SpeechSynthesisRequest) message;
			SpeechSynthesisResponse response = new SpeechSynthesisResponse();
			log.debug(this.getAkibotClient() + ": Speech request: " + request);
			marytts.setVoice(marytssDefaultVoice);

			if (request.getVoice() != null && request.getVoice().length() > 0 && !marytts.getVoice().equals(request.getVoice())) {
				marytts.setVoice(request.getVoice());
			}
			log.debug(this.getAkibotClient() + " Voice: " + marytts.getVoice());

			if (request.getSpeechText() != null && request.getSpeechText().length() > 0) {
				AudioInputStream audio = marytts.generateAudio(request.getSpeechText());
				AudioPlayer player = new AudioPlayer(audio, lineListener);
				player.start();
				player.join();
			}
			response.copySyncId(message);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}

	@Override
	public void start() {
		try {
			marytts = new RemoteMaryInterface(maryttsHost, maryttsPort);
			marytts.setVoice(marytssDefaultVoice);

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
		} catch (IOException e) {
			log.catching(getAkibotClient(), e);
		}

	}

}
