package sandbox.pi4j.motor;

import java.io.IOException;
import java.math.BigDecimal;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

/*
 *  DataSheet: http://www.ti.com/lit/ds/symlink/l293.pdf
 *  https://www.robotix.in/tutorial/auto/motor_driver/
 *  http://garagelab.com/profiles/blogs/tutorial-l293d-h-bridge-dc-motor-controller-with-arduino
 *  http://www.quepublishing.com/articles/article.aspx?p=2271199
 *  https://javatutorial.net/raspberry-pi-dim-led-pwm-java
 */

public class L293D {

	private static final int I2C_ADDRESS = 0x40;

	private static final int BUS_NUMBER = I2CBus.BUS_1;

	/**
	 * Control pin 1 for motor 1.
	 */
	private final Pin input1 = RaspiPin.GPIO_04;

	/**
	 * Control pin 2 for motor 1. Connect this pin to a digital pin on your Arduino.
	 */
	private final Pin input2 = RaspiPin.GPIO_05;

	/**
	 * This “enable pin” should be connected to a digital pin on the RPI. It controls speed by using pulse-width modulation (PWM), a technique for rapidly
	 * triggering a component like an LED or motor, giving it the illusion of dimming or slowing.
	 */
	private final Pin en12 = RaspiPin.GPIO_01;
	private final int en12forWiringPi = 1;

	private final int dummyPwmPin = 2;

	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("STARTING...");
		L293D l293d = new L293D();
		// l293d.simpleRun(); // ok
		// l293d.wiringPiSoftPwmRun(); // ok
		l293d.PCA9685PwmRun(); // ok
		System.out.println("END.");
	}

	/**
	 * Complex testing via PWM (WiringPI). e.g. Run motor at half speed. Tutorial in: https://javatutorial.net/raspberry-pi-dim-led-pwm-java
	 * 
	 * @throws InterruptedException
	 */
	public void wiringPiSoftPwmRun() throws InterruptedException {
		// initialize wiringPi library, this is needed for PWM
		Gpio.wiringPiSetup();
		// softPwmCreate(int pin, int value, int range)
		// the range is set like (min=0 ; max=100)

		SoftPwm.softPwmCreate(en12forWiringPi, 0, 100);
		SoftPwm.softPwmCreate(dummyPwmPin, 0, 200);

		SoftPwm.softPwmWrite(dummyPwmPin, 50);

		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pinInput1 = gpio.provisionDigitalOutputPin(input1, "", PinState.LOW);
		final GpioPinDigitalOutput pinInput2 = gpio.provisionDigitalOutputPin(input2, "", PinState.LOW);

		System.out.println("0 0 0% - stop");
		pinInput1.low();
		pinInput2.low();
		SoftPwm.softPwmWrite(en12forWiringPi, 0);
		Thread.sleep(3000);

		System.out.println("0 1 30% - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		// softPwmWrite(int pin, int value)
		// This updates the PWM value on the given pin. The value is
		// checked to be in-range and pins
		// that haven't previously been initialized via softPwmCreate
		// will be silently ignored.
		SoftPwm.softPwmWrite(en12forWiringPi, 30);
		Thread.sleep(3000);

		System.out.println("0 1 50% - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		SoftPwm.softPwmWrite(en12forWiringPi, 50);
		Thread.sleep(3000);

		SoftPwm.softPwmWrite(dummyPwmPin, 10);

		System.out.println("0 1 100% - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		SoftPwm.softPwmWrite(en12forWiringPi, 100);
		Thread.sleep(3000);

		System.out.println("0 0 0 - stop");
		pinInput1.low();
		pinInput2.low();
		SoftPwm.softPwmWrite(en12forWiringPi, 0);
		Thread.sleep(3000);

		System.out.println("1 0 30% - Clockwise rotation");
		pinInput1.high();
		pinInput2.low();
		SoftPwm.softPwmWrite(en12forWiringPi, 30);
		Thread.sleep(3000);

		System.out.println("0 0 0 - stop");
		pinInput1.low();
		pinInput2.low();
		SoftPwm.softPwmWrite(en12forWiringPi, 0);
		Thread.sleep(3000);

		SoftPwm.softPwmWrite(dummyPwmPin, 0);

	}

	/**
	 * Simple testing via HIGH/LOW signals
	 * 
	 * @throws InterruptedException
	 */
	public void simpleRun() throws InterruptedException {
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pinInput1 = gpio.provisionDigitalOutputPin(input1, "", PinState.LOW);
		final GpioPinDigitalOutput pinInput2 = gpio.provisionDigitalOutputPin(input2, "", PinState.LOW);
		final GpioPinDigitalOutput pinEn12 = gpio.provisionDigitalOutputPin(en12, "", PinState.LOW);

		System.out.println("0 0 0 - stop");
		pinInput1.low();
		pinInput2.low();
		pinEn12.low();
		Thread.sleep(3000);

		System.out.println("0 0 1 - stop");
		pinInput1.low();
		pinInput2.low();
		pinEn12.high();
		Thread.sleep(3000);

		System.out.println("0 1 1 - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		pinEn12.high();
		Thread.sleep(3000);

		System.out.println("1 0 1 - Clockwise rotation");
		pinInput1.high();
		pinInput2.low();
		pinEn12.high();
		Thread.sleep(3000);

		System.out.println("1 1 1 - stop");
		pinInput1.high();
		pinInput2.high();
		pinEn12.high();
		Thread.sleep(3000);

		System.out.println("0 0 0 - stop");
		pinInput1.low();
		pinInput2.low();
		pinEn12.low();
		Thread.sleep(3000);

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

	/**
	 * Complex testing via PWM (Adafruit 16-Channel 12-bit PWM/Servo Driver - I2C interface - PCA9685)
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void PCA9685PwmRun() throws InterruptedException, IOException {
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pinInput1 = gpio.provisionDigitalOutputPin(input1, "", PinState.LOW);
		final GpioPinDigitalOutput pinInput2 = gpio.provisionDigitalOutputPin(input2, "", PinState.LOW);

		System.out.println("Starting...");
		// Took sample from Servos. Not 100% matched, but works.
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
		I2CBus bus = I2CFactory.getInstance(BUS_NUMBER);

		final PCA9685GpioProvider gpioProvider = new PCA9685GpioProvider(bus, I2C_ADDRESS, frequency, frequencyCorrectionFactor);
		// Define outputs in use for this example
		GpioPinPwmOutput[] myOutputs = provisionPwmOutputs(gpioProvider);

		// Reset outputs

		System.out.println("Reset...");
		gpioProvider.reset();

		Pin pinEn12 = PCA9685Pin.PWM_00;

		int PWM_STOP = 1;
		int PWM_SLOW = 7000;
		int PWM_MEDIUM = 12000;
		int PWM_MAX = 20480 - 1;

		System.out.println("0 0 PWM_STOP - stop");
		pinInput1.low();
		pinInput2.low();
		gpioProvider.setPwm(pinEn12, PWM_STOP);
		Thread.sleep(3000);

		System.out.println("0 1 PWM_SLOW - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		gpioProvider.setPwm(pinEn12, PWM_SLOW);
		Thread.sleep(3000);

		System.out.println("0 1 PWM_MEDIUM - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		gpioProvider.setPwm(pinEn12, PWM_MEDIUM);
		Thread.sleep(3000);

		System.out.println("0 1 PWM_MAX - Anti-clockwise rotation");
		pinInput1.low();
		pinInput2.high();
		gpioProvider.setPwm(pinEn12, PWM_MAX);
		Thread.sleep(3000);

		System.out.println("0 0 PWM_STOP - stop");
		pinInput1.low();
		pinInput2.low();
		gpioProvider.setPwm(pinEn12, PWM_STOP);
		Thread.sleep(3000);

		System.out.println("1 0 PWM_SLOW - Clockwise rotation");
		pinInput1.high();
		pinInput2.low();
		gpioProvider.setPwm(pinEn12, PWM_SLOW);
		Thread.sleep(3000);

		System.out.println("0 0 PWM_STOP - stop");
		pinInput1.low();
		pinInput2.low();
		gpioProvider.setPwm(pinEn12, PWM_STOP);
		Thread.sleep(3000);

	}

}
