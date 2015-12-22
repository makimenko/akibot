package com.akibot.tanktrack.launcher;

import com.akibot.tanktrack.component.world.element.Angle;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.pi4j.io.i2c.I2CBus;

public class Constants {
	public static final String DNS_HOST = "raspberrypi";
	public static final int DNS_PORT = 2000;

	public static final String DNS_WEBTEST_HOST = "localhost";
	public static final int DNS_WEBTEST_PORT = 2010;

	public static final int FRONT_SERVO_BASE_PIN = 0;
	public static final int FRONT_SERVO_HEAD_PIN = 7;

	public static final int BACK_SERVO_BASE_PIN = 3;
	public static final int BACK_SERVO_HEAD_PIN = 2;

	public static final int FRONT_DISTANCE_TRIGGER_PIN = 13;
	public static final int FRONT_DISTANCE_ECHO_PIN = 12;
	public static final int FRONT_DISTANCE_TIMEOUT = 50000;
	public static final long DISTANCE_MAX_DISTANCE_MM = 3000;

	public static final int BACK_DISTANCE_TRIGGER_PIN = 21;
	public static final int BACK_DISTANCE_ECHO_PIN = 14;
	public static final int BACK_DISTANCE_TIMEOUT = 50000;

	public static final int TANK_TRACK_RIGHT_IA = 24;
	public static final int TANK_TRACK_RIGHT_IB = 25;
	public static final int TANK_TRACK_LEFT_IA = 28;
	public static final int TANK_TRACK_LEFT_IB = 27;

	public static final int TANK_TRACK_RIGHT_SPEED = 23;
	public static final int TANK_TRACK_LEFT_SPEED = 29;

	public static final int GYROSCOPE_BUS_NUMBER = I2CBus.BUS_1;
	public static final int GYROSCOPE_DEVICE_ADDRESS = 0x1e;

	// (488.0, -84.0, -891.5)
	// public static final double GYROSCOPE_OFFSET_X = 408.0;
	// public static final double GYROSCOPE_OFFSET_Y = -330.0;
	// public static final double GYROSCOPE_OFFSET_Z = -1095.5;
	// public static final double GYROSCOPE_OFFSET_DEGREES = 180;

	public static final int ECHOLOCATOR_FRONT_SLEEP_BEFORE_DISNTANCE = 0;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_TRIGGER_PIN = FRONT_DISTANCE_TRIGGER_PIN;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_ECHO_PIN = FRONT_DISTANCE_ECHO_PIN;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_TIMEOUT = FRONT_DISTANCE_TIMEOUT;
	public static final int ECHOLOCATOR_FRONT_SERVO_HEAD_PIN = 0;
	public static final int ECHOLOCATOR_FRONT_SERVO_BASE_PIN = 1;
	public static final int ECHOLOCATOR_FRONT_SERVO_LONG_TIME = 400;
	public static final int ECHOLOCATOR_FRONT_SERVO_STEP_TIME = 1;
	public static final int ECHOLOCATOR_FRONT_DISTANCE_COUNT = 1;

	public static final int ECHOLOCATOR_BACK_SLEEP_BEFORE_DISNTANCE = 0;
	public static final int ECHOLOCATOR_BACK_DISTANCE_TRIGGER_PIN = BACK_DISTANCE_TRIGGER_PIN;
	public static final int ECHOLOCATOR_BACK_DISTANCE_ECHO_PIN = BACK_DISTANCE_ECHO_PIN;
	public static final int ECHOLOCATOR_BACK_DISTANCE_TIMEOUT = BACK_DISTANCE_TIMEOUT;
	public static final int ECHOLOCATOR_BACK_SERVO_HEAD_PIN = 4;
	public static final int ECHOLOCATOR_BACK_SERVO_BASE_PIN = 5;
	public static final int ECHOLOCATOR_BACK_SERVO_LONG_TIME = 400;
	public static final int ECHOLOCATOR_BACK_SERVO_STEP_TIME = 1;
	public static final int ECHOLOCATOR_BACK_DISTANCE_COUNT = 1;

	public static final String SPEECH_HOST = "192.168.0.106";
	public static final int SPEECH_PORT = 59125;
	public static final String SPEECH_VOICE = "cmu-slt-hsmm";

	public static final String COMPONENT_NAME_AKIBOT_GYROSCOPE = "akibot.gyroscope";
	public static final String COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT = "akibot.echolocator.front";
	public static final String COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK = "akibot.echolocator.back";
	public static final Angle DISTANCE_ERRROR_ANGLE = new Angle(VectorUtils.gradToRad(10));

	public static final String NODE_NAME_GRID = "gridNode";
	public static final String NODE_NAME_ROBOT = "robotNode";

}
