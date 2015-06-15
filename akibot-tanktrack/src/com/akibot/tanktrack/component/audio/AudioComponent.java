package com.akibot.tanktrack.component.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class AudioComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(AudioComponent.class);

	public AudioComponent() {

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof AudioRequest) {
			AudioRequest request = (AudioRequest) message;
			AudioResponse response = new AudioResponse();
			log.debug(this.getAkibotClient() + ": " + request);

			if (request.getAudioUrl() != null) {
				play(request.getAudioUrl());
			}

			response.copySyncId(message);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}

	@Override
	public void start() {

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
			e.printStackTrace();
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
