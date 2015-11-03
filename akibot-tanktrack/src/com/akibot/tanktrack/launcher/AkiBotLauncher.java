package com.akibot.tanktrack.launcher;

import java.net.InetSocketAddress;

import com.akibot.engine2.component.DefaultDNSComponent;
import com.akibot.engine2.component.configuration.ConfigurationComponent;
import com.akibot.engine2.component.test.TestComponent;
import com.akibot.engine2.component.test.TestRequest;
import com.akibot.engine2.component.workflow.WorkflowComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.network.AkibotClient;
import com.akibot.tanktrack.component.audio.AudioComponent;
import com.akibot.tanktrack.component.distance.DistanceMeterComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorComponent;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeComponent;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.calibration.GyroscopeCalibrationComponent;
import com.akibot.tanktrack.component.orientation.OrientationComponent;
import com.akibot.tanktrack.component.scout.ScoutComponent;
import com.akibot.tanktrack.component.servo.ServoComponent;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisComponent;
import com.akibot.tanktrack.component.tanktrack.TankTrackComponent;
import com.akibot.tanktrack.component.world.WorldComponent;

public class AkiBotLauncher {
	static final AkiLogger log = AkiLogger.create(AkiBotLauncher.class);

	public static void main(String[] args) throws Exception {
		String dnsHost = Constants.DNS_HOST;
		int dnsPort = Constants.DNS_PORT;

		InetSocketAddress dnsAddress = new InetSocketAddress(dnsHost, dnsPort);

		// DNS:
		AkibotClient dns = new AkibotClient("akibot.dns", new DefaultDNSComponent(), dnsPort);
		dns.start();
		AkibotClient configClient = new AkibotClient("akibot.config", new ConfigurationComponent("."), dnsAddress);
		configClient.start();

		// TankTrack:
		AkibotClient tankTrack = new AkibotClient("akibot.tanktrack", new TankTrackComponent(), dnsAddress);
		// Gyroscope:
		AkibotClient gyroscope = new AkibotClient(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE, new GyroscopeComponent(), dnsAddress);
		// SpeechSynthesis:
		AkibotClient speechSynthesisClient = new AkibotClient("akibot.speech", new SpeechSynthesisComponent(), dnsAddress);
		// Distance Meter
		AkibotClient distance = new AkibotClient("akibot.front.distance", new DistanceMeterComponent(), dnsAddress);
		// Servo motors
		AkibotClient servoFrontBase = new AkibotClient("akibot.servo.front.base", new ServoComponent(), dnsAddress);
		AkibotClient servoFrontHead = new AkibotClient("akibot.servo.front.head", new ServoComponent(), dnsAddress);
		AkibotClient servoBackBase = new AkibotClient("akibot.servo.back.base", new ServoComponent(), dnsAddress);
		AkibotClient servoBackHead = new AkibotClient("akibot.servo.back.head", new ServoComponent(), dnsAddress);
		//
		AkibotClient testComponent = new AkibotClient("akibot.test", new TestComponent(), dnsAddress);
		testComponent.getMyClientDescription().getTopicList().add(new TestRequest());
		AkibotClient audioComponent = new AkibotClient("akibot.audio", new AudioComponent(), dnsAddress);
		AkibotClient echoLocatorFront = new AkibotClient(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT, new EchoLocatorComponent(), dnsAddress);
		AkibotClient echoLocatorBack = new AkibotClient(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK, new EchoLocatorComponent(), dnsAddress);
		AkibotClient orientation = new AkibotClient("akibot.orientation", new OrientationComponent(), dnsAddress);
		AkibotClient gyroscopeCalibration = new AkibotClient("akibot.gyroscope.calibration", new GyroscopeCalibrationComponent(), dnsAddress);
		AkibotClient worldClient = new AkibotClient("akibot.world", new WorldComponent(), dnsAddress);
		// AkibotClient statusWatchdogClient = new AkibotClient("akibot.status.watchdog", new StatusWatchdogComponent(1 * 1000, 5 * 1000), dnsAddress);
		AkibotClient workflowClient = new AkibotClient("akibot.workflow", new WorkflowComponent(), dnsAddress);
		workflowClient.getComponent().addTopic(new EchoLocatorResponse());
		workflowClient.getComponent().addTopic(new GyroscopeResponse());
		AkibotClient scoutClient = new AkibotClient("akibot.scout", new ScoutComponent(), dnsAddress);

		// Start all
		Thread.sleep(500);

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
		gyroscopeCalibration.start();
		scoutClient.start();
		workflowClient.start();
		worldClient.start();

		// TODO: temporary disabled: statusWatchdogClient.start();

		System.out.println("AkiBotLauncher: Started");
		// LOOP forever:
		while (true) {
			Thread.sleep(10000);
		}

	}

}
