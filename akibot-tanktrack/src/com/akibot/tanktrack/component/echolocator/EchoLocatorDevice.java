package com.akibot.tanktrack.component.echolocator;

import java.io.IOException;

import akibot.jni.java.AkibotJniLibraryInstance000;

import com.akibot.engine2.exception.FailedToConfigureException;
import com.akibot.engine2.logger.AkiLogger;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public class EchoLocatorDevice {
	static final AkiLogger log = AkiLogger.create(EchoLocatorDevice.class);

	private EchoLocatorConfiguration config;
	private boolean initialized = false;
	private I2CBus bus;
	private PCA9685GpioProvider gpioProvider;
	private GpioPinPwmOutput[] myOutputs;
	private AkibotJniLibraryInstance000 lib;
	private int lastServoBasePosition;
	private int lastServoHeadPosition;
	private Pin servoBasePin;
	private Pin servoHeadPin;

	public EchoLocatorDevice(EchoLocatorConfiguration config) {
		this.config = config;
		init();
	}

	private void init() {
		initialized = false;

		try {
			log.debug("EcholocatorDevice Initializing...");
			bus = I2CFactory.getInstance(config.getBusNumber());
			gpioProvider = new PCA9685GpioProvider(bus, config.getAddress(), config.getFrequency(), config.getFrequencyCorrectionFactor());
			myOutputs = provisionPwmOutputs(gpioProvider);
			gpioProvider.reset();

			servoBasePin = getPwmPin(config.getServoBasePin());
			servoHeadPin = getPwmPin(config.getServoHeadPin());

			if (config.getDistanceEchoPin() > 0 && config.getDistanceCount() > 0 && config.getDistanceTriggerPin() > 0 && config.getDistanceTimeout() > 0
					&& config.getServoLongTime() >= 0 && config.getServoMax() > 0 && config.getServoMin() >= 0 && config.getServoStepTime() >= 0
					&& config.getSleepBeforeDistance() >= 0) {
				initialized = true;
			}

			lib = new AkibotJniLibraryInstance000();
			lib.initialize();

		} catch (IOException | FailedToConfigureException e) {
			log.catching(e);
		}
		log.debug("EcholocatorDevice initialization status: " + initialized);

	}

	private Pin getPwmPin(int pinAddress) throws FailedToConfigureException {
		switch (pinAddress) {
		case 0:
			return PCA9685Pin.PWM_00;
		case 1:
			return PCA9685Pin.PWM_01;
		case 2:
			return PCA9685Pin.PWM_02;
		case 3:
			return PCA9685Pin.PWM_03;
		case 4:
			return PCA9685Pin.PWM_04;
		case 5:
			return PCA9685Pin.PWM_05;
		case 6:
			return PCA9685Pin.PWM_06;
		case 7:
			return PCA9685Pin.PWM_07;
		case 8:
			return PCA9685Pin.PWM_08;
		case 9:
			return PCA9685Pin.PWM_09;
		case 10:
			return PCA9685Pin.PWM_10;
		case 11:
			return PCA9685Pin.PWM_11;
		case 12:
			return PCA9685Pin.PWM_12;
		case 13:
			return PCA9685Pin.PWM_13;
		case 14:
			return PCA9685Pin.PWM_14;
		case 15:
			return PCA9685Pin.PWM_15;
		default:
			throw new FailedToConfigureException("Unknown PWM PIN Address: " + pinAddress);

		}
	}

	public float[] scanDistance(int servoBaseFrom, int servoBaseTo, int servoBaseStep, int servoHeadNormal, boolean trustToLastPosition) throws Exception {
		if (!initialized) {
			throw new FailedToConfigureException("EcholocatorDevice is not initialized!");
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
		log.trace("servoBaseFrom = " + servoBaseFrom + ", servoBaseTo = " + servoBaseTo + ", servoBaseStep = " + servoBaseStep + ", size = " + size);

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
		
		synchronized (gpio) {
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
}