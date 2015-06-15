package com.akibot.tanktrack.launcher;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class TestSound {
	public static void main(String[] args) {
		AudioInputStream din = null;
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new URL(
					"https://www.callfire.com/cloud/1/files/recording/dynamic/864c81f4a8e6a898042cb47251ca1c85/2012/8/6065312/cid_1319779221_1.wav"));
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
		System.out.println("END");
	}

}