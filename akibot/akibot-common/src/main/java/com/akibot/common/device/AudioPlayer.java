package com.akibot.common.device;

public interface AudioPlayer extends DeviceInterface {

	public void play(String audioUrl);

	public void stop();

}
