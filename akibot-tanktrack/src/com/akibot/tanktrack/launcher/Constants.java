package com.akibot.tanktrack.launcher;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;

public class Constants {
	public static final int FRONT_SERVO_BASE_PIN = 0;
	public static final int FRONT_SERVO_HEAD_PIN = 7;

	public static final int BACK_SERVO_BASE_PIN = 3;
	public static final int BACK_SERVO_HEAD_PIN = 2;

	public static final int FRONT_DISTANCE_TRIGGER_PIN = 13;
	public static final int FRONT_DISTANCE_ECHO_PIN = 12;
	public static final int FRONT_DISTANCE_TIMEOUT = 50000;

	public static final int BACK_DISTANCE_TRIGGER_PIN = 21;
	public static final int BACK_DISTANCE_ECHO_PIN = 14;
	public static final int BACK_DISTANCE_TIMEOUT = 50000;

	public static final Pin TANK_TRACK_RIGHT_IA = RaspiPin.GPIO_24;
	public static final Pin TANK_TRACK_RIGHT_IB = RaspiPin.GPIO_25;
	public static final Pin TANK_TRACK_LEFT_IA = RaspiPin.GPIO_28;
	public static final Pin TANK_TRACK_LEFT_IB = RaspiPin.GPIO_27;

	public static final int GYROSCOPE_BUS_NUMBER = I2CBus.BUS_1;
	public static final int GYROSCOPE_DEVICE_ADDRESS = 0x1e;
	// com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationResponse:
	// new offset = (709.0, 220.0, -872.0)
	// (557.0, 294.5, -801.5)
	// 729.5, 111.5, -1415.0
	// (345.5, 279.5, -1483.0)
	// 522.0, 51.5, -1010.0
	// 2015.07.01: 368.5, 54.5, -832.0
	// 533.0, -208.0, -710.5
	// (488.0, -84.0, -891.5)
	public static final double GYROSCOPE_OFFSET_X = 488.0;
	public static final double GYROSCOPE_OFFSET_Y = -84.0;
	public static final double GYROSCOPE_OFFSET_Z = -891.5;
	public static final double GYROSCOPE_OFFSET_DEGREES = 180;

	public static final int ECHOLOCATOR_FRONT_SLEEP_BEFORE_DISNTANCE = 50000;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_TRIGGER_PIN = FRONT_DISTANCE_TRIGGER_PIN;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_ECHO_PIN = FRONT_DISTANCE_ECHO_PIN;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_TIMEOUT = FRONT_DISTANCE_TIMEOUT;
	public static final int ECHOLOCATOR_FRONT_SERVO_HEAD_PIN = FRONT_SERVO_HEAD_PIN;
	public static final int ECHOLOCATOR_FRONT_SERVO_BASE_PIN = FRONT_SERVO_BASE_PIN;
	public static final int ECHOLOCATOR_FRONT_SERVO_LONG_TIME = 400000;
	public static final int ECHOLOCATOR_FRONT_SERVO_STEP_TIME = 35000;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_COUNT = 1;

	public static final int ECHOLOCATOR_BACK_SLEEP_BEFORE_DISNTANCE = 50000;
	public static final int ECHOLOCATOR_BACK_DISTANCE_TRIGGER_PIN = BACK_DISTANCE_TRIGGER_PIN;
	public static final int ECHOLOCATOR_BACK_DISTANCE_ECHO_PIN = BACK_DISTANCE_ECHO_PIN;
	public static final int ECHOLOCATOR_BACK_DISTANCE_TIMEOUT = BACK_DISTANCE_TIMEOUT;
	public static final int ECHOLOCATOR_BACK_SERVO_HEAD_PIN = BACK_SERVO_HEAD_PIN;
	public static final int ECHOLOCATOR_BACK_SERVO_BASE_PIN = BACK_SERVO_BASE_PIN;
	public static final int ECHOLOCATOR_BACK_SERVO_LONG_TIME = 400000;
	public static final int ECHOLOCATOR_BACK_SERVO_STEP_TIME = 35000;
	public static final int ECHOLOCATOR_BACK_DISTANCE_COUNT = 1;

	public static final String SPEECH_HOST = "192.168.0.106";
	public static final int SPEECH_PORT = 59125;
	public static final String SPEECH_VOICE = "cmu-slt-hsmm";

}
