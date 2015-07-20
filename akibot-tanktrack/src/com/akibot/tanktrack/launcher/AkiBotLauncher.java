package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.engine2.test.component.TestComponent;
import com.akibot.engine2.test.component.TestRequest;
import com.akibot.tanktrack.component.audio.AudioComponent;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorComponent;
import com.akibot.tanktrack.component.gyroscope.GyroscopeComponent;
import com.akibot.tanktrack.component.orientation.OrientationComponent;
import com.akibot.tanktrack.component.servo.ServoComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisComponent;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;

public class AkiBotLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotLauncher.class);

	public static void main(String[] args) throws Exception {
		String dnsHost = Constants.DNS_HOST;
		int dnsPort = Constants.DNS_PORT;

		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		// DNS:
		AkibotClient dns = new AkibotClient("akibot.dns", new DefaultDNSComponent(), dnsPort);
		dns.start();

		// ConfigurationComponent:
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("."), dnsAddress);

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new TankTrackComponent(Constants.TANK_TRACK_RIGHT_IA, Constants.TANK_TRACK_RIGHT_IB,
				Constants.TANK_TRACK_RIGHT_SPEED, Constants.TANK_TRACK_LEFT_IA, Constants.TANK_TRACK_LEFT_IB, Constants.TANK_TRACK_LEFT_SPEED), dnsAddress);

		// Gyroscope:
		AkibotClient gyroscope = new AkibotClient("akibot.gyroscope", new GyroscopeComponent(), dnsAddress);

		// SpeechSynthesis:
		AkibotClient speechSynthesisClient = new AkibotClient("akibot.speech", new SpeechSynthesisComponent(), dnsAddress);

		// Distance Meter
		AkibotClient distance = new AkibotClient("akibot.front.distance", new DistanceMeterComponent(), dnsAddress);

		// Servo motors
		AkibotClient servoFrontBase = new AkibotClient("akibot.servo.front.base", new ServoComponent(Constants.FRONT_SERVO_BASE_PIN, 0, 200, 200), dnsAddress);

		AkibotClient servoFrontHead = new AkibotClient("akibot.servo.front.head", new ServoComponent(Constants.FRONT_SERVO_HEAD_PIN, 0, 200, 200), dnsAddress);

		AkibotClient servoBackBase = new AkibotClient("akibot.servo.back.base", new ServoComponent(Constants.BACK_SERVO_BASE_PIN, 0, 200, 200), dnsAddress);

		AkibotClient servoBackHead = new AkibotClient("akibot.servo.back.head", new ServoComponent(Constants.BACK_SERVO_HEAD_PIN, 0, 200, 200), dnsAddress);

		AkibotClient testComponent = new AkibotClient("akibot.test", new TestComponent(), dnsAddress);
		testComponent.getMyClientDescription().getTopicList().add(new TestRequest());

		AkibotClient audioComponent = new AkibotClient("akibot.audio", new AudioComponent(), dnsAddress);
		AkibotClient echoLocatorFront = new AkibotClient("akibot.echolocator.front", new EchoLocatorComponent(), dnsAddress);
		AkibotClient echoLocatorBack = new AkibotClient("akibot.echolocator.back", new EchoLocatorComponent(), dnsAddress);

		AkibotClient orientation = new AkibotClient("akibot.orientation", new OrientationComponent("akibot.tanktrack", "akibot.gyroscope"), dnsAddress);

		// Start all
		configClient.start();
		Thread.sleep(1000);

		tankTrack.start();
		gyroscope.start();
		speechSynthesisClient.start();
		distance.start();
		// obstacle.start();
		servoFrontBase.start();
		servoFrontHead.start();
		servoBackHead.start();
		servoBackBase.start();
		testComponent.start();
		echoLocatorFront.start();
		echoLocatorBack.start();
		audioComponent.start();
		orientation.start();

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
