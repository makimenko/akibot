package sandbox.pi4j.servodriver;

import java.math.BigDecimal;
import java.util.Scanner;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;

public class PCA9685GpioExample {

	private static final int SERVO_DURATION_MIN = 600;
	private static final int SERVO_DURATION_MAX = 2500;
	private static final int SERVO_DURATION_NEUTRAL = SERVO_DURATION_MIN + ((SERVO_DURATION_MAX - SERVO_DURATION_MIN) / 2);

	public static void main(String args[]) throws Exception {

		System.out.println("Starting...");
		// This would theoretically lead into a resolution of 5 microseconds per step:
		// 4096 Steps (12 Bit)
		// T = 4096 * 0.000005s = 0.02048s
		// f = 1 / T = 48.828125
		BigDecimal frequency = new BigDecimal("48.828");
		// Correction factor: actualFreq / targetFreq
		// e.g. measured actual frequency is: 51.69 Hz
		// Calculate correction factor: 51.65 / 48.828 = 1.0578
		// --> To measure actual frequency set frequency without correction factor(or set to 1)
		BigDecimal frequencyCorrectionFactor = new BigDecimal("1.0578");
		// Create custom PCA9685 GPIO provider
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);

		final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus, 0x40, frequency, frequencyCorrectionFactor);
		// Define outputs in use for this example
		GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider);
		// Reset outputs

		System.out.println("Reset...");
		gpioProvider.reset();

		Pin pin = PCA9685Pin.PWM_15;
		
		Pin pinHead = PCA9685Pin.PWM_00;
		
		System.out.println("Set PWM...");
		gpioProvider.setPwm(pin, SERVO_DURATION_MIN);
		gpioProvider.setPwm(pinHead, SERVO_DURATION_MIN);
		System.out.println("Sleeping...");
		Thread.sleep(1000);


		System.out.println("Set PWM...");
		gpioProvider.setPwm(pin, SERVO_DURATION_MAX);
		gpioProvider.setPwm(pinHead, SERVO_DURATION_MAX);
		System.out.println("Sleeping...");
		Thread.sleep(1000);

		System.out.println("Set PWM...");
		gpioProvider.setPwm(pin, SERVO_DURATION_NEUTRAL);
		gpioProvider.setPwm(pinHead, SERVO_DURATION_NEUTRAL);
		System.out.println("Sleeping...");
		Thread.sleep(1000);

		
		// Show PWM values for outputs 0..14
		for (GpioPinPwmOutput output : myOutputs) {
			int[] onOffValues = gpioProvider.getPwmOnOffValues(output.getPin());
			System.out
					.println(output.getPin().getName() + " (" + output.getName() + "): ON value [" + onOffValues[0] + "], OFF value [" + onOffValues[1] + "]");
		}

		Thread.sleep(5000);
		
		System.out.println("DONE");
	}

	private static int checkForOverflow(int position) {
		int result = position;
		if (position > PCA9685GpioProvider.PWM_STEPS - 1) {
			result = position - PCA9685GpioProvider.PWM_STEPS - 1;
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