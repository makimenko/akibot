package sandbox.speech.synthesis;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.util.data.audio.AudioPlayer;

public class TestRemoteSpeech {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String hostname = "192.168.0.101";
		int port = 59125;

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

		System.out.println("1");
		MaryInterface marytts = new RemoteMaryInterface(hostname, port);
		System.out.println("2");
		marytts.setVoice("cmu-slt-hsmm");
		System.out.println("3");
		AudioInputStream audio = marytts.generateAudio("Hello world. My name is Michael! What is your name?");
		System.out.println("4");
		AudioPlayer player = new AudioPlayer(audio, lineListener);
		System.out.println("5");
		player.start();
		System.out.println("6");
		player.join();
		System.out.println("7");
	}
}