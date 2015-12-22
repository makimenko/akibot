package sandbox.jni;

import akibot.jni.java.AkibotJniLibrary;
import akibot.jni.java.AkibotJniLibraryInstance000;

public class TestEchoLocator {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("STARTING....");
		AkibotJniLibrary lib = new AkibotJniLibraryInstance000();
		lib.initialize();
		float[] result;

		System.out.println("===============");
		int _distanceTriggerPin = 13;
		int _distanceEchoPin = 12;
		int _distanceTimeout = 50000;
		int _sleepBeforeDistance = 5000;
		int _servoI2CBus = 0;
		int _servoI2CAddress = 0x40;
		int _servoI2CFrequency = 61;
		int _servoBasePin = 1;
		int _servoHeadPin = 0;
		int _servoLongTime = 400000;
		int _servoStepTime = 3000;
		int _distanceCount = 1;

		lib.echoLocatorInitialize(_distanceTriggerPin, _distanceEchoPin, _distanceTimeout, _sleepBeforeDistance, _servoI2CBus, _servoI2CAddress,
				_servoI2CFrequency, _servoBasePin, _servoHeadPin, _servoLongTime, _servoStepTime, _distanceCount);

		int servoBaseFrom = 150;
		int servoBaseTo = 650;
		int servoBaseStep = 50;
		int servoHeadNormal = 370;
		boolean trustToLastPosition = true;

		result = lib.echoLocatorScanDistance(servoBaseFrom, servoBaseTo, servoBaseStep, servoHeadNormal, trustToLastPosition);
		for (int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}

		System.out.println("END");
	}

}
