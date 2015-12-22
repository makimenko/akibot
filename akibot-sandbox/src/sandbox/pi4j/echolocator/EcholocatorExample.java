package sandbox.pi4j.echolocator;

import java.math.BigDecimal;

public class EcholocatorExample {

	public static void main(String[] args) throws Exception {
		EcholocatorDeviceConfiguration config = new EcholocatorDeviceConfiguration();
		config.setBusNumber(0);
		config.setAddress(0x40);
		config.setDistanceCount(1);
		config.setDistanceEchoPin(12);
		config.setDistanceTimeout(50000);
		config.setDistanceTriggerPin(13);
		config.setFrequency(new BigDecimal("48.828"));
		config.setFrequencyCorrectionFactor(new BigDecimal("1.0578"));
		config.setServoBasePin(1);
		config.setServoHeadPin(0);
		config.setServoLongTime(400000 / 1000);
		config.setServoMin(600);
		config.setServoMax(2500);
		config.setServoStepTime(3000 / 1000);
		config.setSleepBeforeDistance(5000 / 1000);

		EcholocatorDevice echolocator = new EcholocatorDevice(config);

		// Request Echolocator:
		int servoBaseFrom = 600;
		int servoBaseTo = 2500;
		int servoBaseStep = 100;
		int servoHeadNormal = 950;
		boolean trustToLastPosition = true;
		float result[] = echolocator.scanDistance(servoBaseFrom, servoBaseTo, servoBaseStep, servoHeadNormal, trustToLastPosition);

		for (float i : result) {
			System.out.println("Distance = " + i);
		}

	}

}