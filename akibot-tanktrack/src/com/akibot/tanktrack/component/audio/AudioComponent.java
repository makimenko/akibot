package com.akibot.tanktrack.component.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class AudioComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AudioComponent.class);

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof AudioRequest) {
			onAudioRequest((AudioRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	@Override
	public void loadDefaults() {
		addTopic(new AudioRequest());
		getComponentStatus().setReady(true);
	}

	private void onAudioRequest(AudioRequest audioRequest) throws FailedToSendMessageException {
		log.debug(this.getAkibotClient() + ": " + audioRequest);
		AudioResponse response = new AudioResponse();
		if (audioRequest.getAudioUrl() != null) {
			play(audioRequest.getAudioUrl());
		}
		broadcastResponse(response, audioRequest);
	}

	private void play(String audioUrl) {
		AudioInputStream din = null;
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new URL(audioUrl));
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			if (line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();

				int nBytesRead;
				while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
					line.write(data, 0, nBytesRead);
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}
		} catch (Exception e) {
			log.catching(getAkibotClient(), e);
		} finally {
			if (din != null) {
				try {
					din.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
