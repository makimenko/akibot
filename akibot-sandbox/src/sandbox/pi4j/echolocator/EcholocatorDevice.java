package sandbox.pi4j.echolocator;

import java.io.IOException;

import akibot.jni.java.AkibotJniLibraryInstance000;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public class EcholocatorDevice {

	private EcholocatorDeviceConfiguration config;
	private boolean initialized = false;
	private I2CBus bus;
	private PCA9685GpioProvider gpioProvider;
	private GpioPinPwmOutput[] myOutputs;
	private AkibotJniLibraryInstance000 lib;
	private int lastServoBasePosition;
	private int lastServoHeadPosition;
	private Pin servoBasePin;
	private Pin servoHeadPin;

	//

	public EcholocatorDevice(EcholocatorDeviceConfiguration config) {
		this.config = config;
		init();
	}

	private void init() {
		initialized = false;

		try {
			System.out.println("EcholocatorDevice Initializing...");
			bus = I2CFactory.getInstance(config.getBusNumber());
			gpioProvider = new PCA9685GpioProvider(bus, config.getAddress(), config.getFrequency(), config.getFrequencyCorrectionFactor());
			myOutputs = provisionPwmOutputs(gpioProvider);
			gpioProvider.reset();

			servoHeadPin = PCA9685Pin.PWM_00; // TODO: add to config
			servoBasePin = PCA9685Pin.PWM_01; // TODO: add to config

			if (config.getDistanceEchoPin() > 0 && config.getDistanceCount() > 0 && config.getDistanceTriggerPin() > 0 && config.getDistanceTimeout() > 0
					&& config.getServoBasePin() >= 0 && config.getServoHeadPin() >= 0 && config.getServoLongTime() >= 0 && config.getServoMax() > 0
					&& config.getServoMin() >= 0 && config.getServoStepTime() >= 0 && config.getSleepBeforeDistance() >= 0) {
				initialized = true;
			}

			lib = new AkibotJniLibraryInstance000();
			lib.initialize();

			System.out.println("EcholocatorDevice initialization status: " + initialized);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public float[] scanDistance(int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, boolean trustToLastPosition) throws Exception {
		if (!initialized) {
			throw new Exception("EcholocatorDevice is not initialized!");
		}

		int step;
		int size;
		int currentPosition = servoBaseFrom;
		if (servoBaseFrom < servoBaseTo) {
			size = (servoBaseTo - servoBaseFrom) / servoBaseStep + 1;
			step = servoBaseStep;
		} else {
			size = (servoBaseFrom - servoBaseTo) / servoBaseStep + 1;
			step = -servoBaseStep;
		}
		System.out.println("servoBaseFrom = " + servoBaseFrom);
		System.out.println("servoBaseTo = " + servoBaseTo);
		System.out.println("servoBaseStep = " + servoBaseStep);
		System.out.println("size = " + size);

		float[] result = new float[size];

		// Move to start position only if needed:
		if (!(trustToLastPosition && lastServoBasePosition == servoBaseFrom && lastServoHeadPosition == servoHeadNormal)) {
			gpioProvider.setPwm(servoBasePin, servoBaseFrom);
			gpioProvider.setPwm(servoHeadPin, servoHeadNormal);
			Thread.sleep(config.getServoLongTime());
			lastServoBasePosition = servoBaseFrom;
			lastServoHeadPosition = servoHeadNormal;
		}

		int index = 0;
		for (int i = 1; i <= size; i++) {
			gpioProvider.setPwm(servoBasePin, currentPosition);
			Thread.sleep(config.getServoStepTime() * servoBaseStep);

			if (config.getSleepBeforeDistance() > 0) {
				Thread.sleep(config.getSleepBeforeDistance());
			}
			float totalDistance = 0;
			float avgDistance = 0;
			for (int j = 1; j <= config.getDistanceCount(); j++) {
				float dist = lib.getDistance(config.getDistanceTriggerPin(), config.getDistanceEchoPin(), config.getDistanceTimeout());
				totalDistance += dist;
			}
			avgDistance = totalDistance / config.getDistanceCount();
			result[index] = avgDistance;
			index++;

			currentPosition += step;
			lastServoBasePosition = currentPosition;
		}

		return result;
	}

	private static GpioPinPwmOutput[] provisionPwmOutputs(final PCA9685GpioProvider gpioProvider) {
		GpioController gpio = GpioFactory.getInstance();
		GpioPinPwmOutput myOutputs[] = { gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Pulse 00"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Pulse 01"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Pulse 02"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Pulse 03"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Pulse 04"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Pulse 05"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Pulse 06"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Pulse 07"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Pulse 08"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Pulse 09"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Pulse 10"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Pulse 11"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Pulse 12"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Pulse 13"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Pulse 14"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "Pulse 15") };
		return myOutputs;
	}
}