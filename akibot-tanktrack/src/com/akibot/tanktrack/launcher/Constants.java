package com.akibot.tanktrack.launcher;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

public class Constants {
	public static int FRONT_SERVO_BASE_PIN = 0;
	public static int FRONT_SERVO_HEAD_PIN = 7;
	
	public static int FRONT_DISTANCE_TRIGGER_PIN = 13;
	public static int FRONT_DISTANCE_ECHO_PIN = 12;
	
	public static Pin TANK_TRACK_RIGHT_IA = RaspiPin.GPIO_24;
	public static Pin TANK_TRACK_RIGHT_IB = RaspiPin.GPIO_25;
	public static Pin TANK_TRACK_LEFT_IA = RaspiPin.GPIO_28;
	public static Pin TANK_TRACK_LEFT_IB = RaspiPin.GPIO_27;
	
	public static int GYROSCOPE_BUS_NUMBER = I2CBus.BUS_1;
	public static int GYROSCOPE_DEVICE_ADDRESS = 0x1e;
	public static double GYROSCOPE_OFFSET_X = 58.0;
	public static double GYROSCOPE_OFFSET_Y = -47.5;
	public static double GYROSCOPE_OFFSET_Z = 129.5;
	public static double GYROSCOPE_OFFSET_DEGREES = 180;
	
		
}
