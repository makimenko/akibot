package com.akibot.common.device;

public interface AudioPlayer extends DeviceInterface {

	void play(String audioUrl);

	void stop();

}
