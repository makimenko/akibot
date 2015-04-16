package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultServerComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.distance.DistanceResponse;
import com.akibot.tanktrack.component.echolocator.EchoLocatorComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorConfig;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeRequest;
import com.akibot.tanktrack.component.gyroscope.HMC5883LGyroscopeComponent;
import com.akibot.tanktrack.component.servo.ServoComponent;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.servo.ServoResponse;
import com.akibot.tanktrack.component.tanktrack.DD1TankTrackComponent;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;

public class AkiBotLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotLauncher.class);

	public static void main(String[] args) throws Exception {
		String serverHost = "raspberrypi";
		int serverPort = 2000;

		InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);

		// Server:
		AkibotClient server = new AkibotClient("akibot.server", new DefaultServerComponent(), serverPort);
		server.start();

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new DD1TankTrackComponent(Constants.TANK_TRACK_RIGHT_IA, Constants.TANK_TRACK_RIGHT_IB,
				Constants.TANK_TRACK_LEFT_IA, Constants.TANK_TRACK_LEFT_IB), serverAddress);
		tankTrack.getMyClientDescription().getTopicList().add(new StickMotionRequest());

		// Gyroscope:
		AkibotClient gyroscope = new AkibotClient("akibot.gyroscope", new HMC5883LGyroscopeComponent(Constants.GYROSCOPE_BUS_NUMBER,
				Constants.GYROSCOPE_DEVICE_ADDRESS, Constants.GYROSCOPE_OFFSET_X, Constants.GYROSCOPE_OFFSET_Y, Constants.GYROSCOPE_OFFSET_Z,
				Constants.GYROSCOPE_OFFSET_DEGREES), serverAddress);
		gyroscope.getMyClientDescription().getTopicList().add(new GyroscopeRequest());

		// SpeechSynthesis:
		// String maryttsHost = "192.168.0.102";
		// int maryttsPort = 59125;
		// String maryttsVoice = "cmu-slt-hsmm";
		// SpeechSynthesisComponent speechSynthesisComponent = new
		// SpeechSynthesisComponent(maryttsHost, maryttsPort, maryttsVoice);
		// ClientDescription speechSynthesisDescription = new
		// ClientDescription("akibot.speech.synthesis");
		// speechSynthesisDescription.getTopicList().add(new
		// SpeechSynthesisRequest());
		// Client speechSynthesisClient = new Client(akibotHost, akibotPort,
		// speechSynthesisComponent, speechSynthesisDescription);

		// Distance Meter
		AkibotClient distance = new AkibotClient("akibot.distance", new DistanceMeterComponent(Constants.FRONT_DISTANCE_TRIGGER_PIN,
				Constants.FRONT_DISTANCE_ECHO_PIN, Constants.FRONT_DISTANCE_TIMEOUT), serverAddress);
		distance.getMyClientDescription().getTopicList().add(new DistanceRequest());

		// Obstacle:
		// AkibotClient obstacle = new AkibotClient("akibot.obstacle", new
		// ObstacleComponent(), serverAddress);
		// obstacle.getMyClientDescription().getTopicList().add(new
		// ObstacleRequest());
		// obstacle.getMyClientDescription().getTopicList().add(new
		// GyroscopeResponse());
		// obstacle.getMyClientDescription().getTopicList().add(new
		// DistanceResponse());

		// Servo motors
		AkibotClient servoBase = new AkibotClient("akibot.servo.base", new ServoComponent(Constants.FRONT_SERVO_BASE_PIN, 0, 200, 200), serverAddress);
		servoBase.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient servoHead = new AkibotClient("akibot.servo.head", new ServoComponent(Constants.FRONT_SERVO_HEAD_PIN, 0, 200, 200), serverAddress);
		servoHead.getMyClientDescription().getTopicList().add(new ServoRequest());

		AkibotClient testComponent = new AkibotClient("akibot.test", new TestComponent(), serverAddress);
		testComponent.getMyClientDescription().getTopicList().add(new TestRequest());

		// EchoLocator:
		EchoLocatorConfig echoLocatorConfig = new EchoLocatorConfig();

		echoLocatorConfig.setDistanceTriggerPin(Constants.ECHOLOCATOR_DISTANCE_TRIGGER_PIN);
		echoLocatorConfig.setDistanceEchoPin(Constants.ECHOLOCATOR_DISTANCE_ECHO_PIN);
		echoLocatorConfig.setDistanceTimeout(Constants.ECHOLOCATOR_DISTANCE_TIMEOUT);
		echoLocatorConfig.setSleepBeforeDistance(Constants.ECHOLOCATOR_SLEEP_BEFORE_DISNTANCE);
		echoLocatorConfig.setServoBasePin(Constants.ECHOLOCATOR_SERVO_BASE_PIN);
		echoLocatorConfig.setServoHeadPin(Constants.ECHOLOCATOR_SERVO_HEAD_PIN);
		// TODO: Put into request servoLongTime?:
		echoLocatorConfig.setServoLongTime(Constants.ECHOLOCATOR_SERVO_LONG_TIME);
		echoLocatorConfig.setServoStepTime(Constants.ECHOLOCATOR_SERVO_STEP_TIME);
		echoLocatorConfig.setDistanceCount(Constants.ECHOLOCATOR_DISTANCE_COUNT);
		// 13, 12, 500000, 50000, 0, 7,
		// 4, 24, 1, 14,
		// 400000, 35000, 1, true);
		AkibotClient echoLocator = new AkibotClient("akibot.echolocator", new EchoLocatorComponent(echoLocatorConfig), serverAddress);
		echoLocator.getMyClientDescription().getTopicList().add(new EchoLocatorRequest());
		echoLocator.getMyClientDescription().getTopicList().add(new DistanceResponse());
		echoLocator.getMyClientDescription().getTopicList().add(new ServoResponse());

		// Start all
		tankTrack.start();
		gyroscope.start();
		// speechSynthesisClient.start();
		distance.start();
		// obstacle.start();
		servoBase.start();
		servoHead.start();
		testComponent.start();
		echoLocator.start();

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
