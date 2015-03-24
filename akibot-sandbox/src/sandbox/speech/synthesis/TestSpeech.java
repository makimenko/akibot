package sandbox.speech.synthesis;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.util.data.audio.AudioPlayer;

public class TestSpeech {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		MaryInterface marytts = new LocalMaryInterface();
		System.out.println("Generating...");

		LineListener lineListener = new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.START) {
					System.err.println("Audio started playing.");
				} else if (event.getType() == LineEvent.Type.STOP) {
					System.err.println("Audio stopped playing.");
				} else if (event.getType() == LineEvent.Type.OPEN) {
					System.err.println("Audio line opened.");
				} else if (event.getType() == LineEvent.Type.CLOSE) {
					System.err.println("Audio line closed.");
				}
			}
		};

		marytts.setVoice("cmu-slt-hsmm");
		AudioInputStream audio = marytts.generateAudio("Hello! My name is Akibot and I'm ready to help you!");
		System.out.println("..generated.");

		AudioPlayer player = new AudioPlayer(audio, lineListener);
		player.start();
		player.join();

	}
}