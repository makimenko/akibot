package sandbox.pi4j.tanktrack;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SandboxTankTrack {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Starting...");
		final GpioController gpio = GpioFactory.getInstance();
		System.out.println("Instance initialized.");

		final GpioPinDigitalOutput rightBackward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "rightIA", PinState.LOW);
		final GpioPinDigitalOutput rightForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "rightIB", PinState.LOW);

		final GpioPinDigitalOutput leftBackward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "leftIA", PinState.LOW);
		final GpioPinDigitalOutput leftForward = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "leftIB", PinState.LOW);

		leftForward.high();
		rightForward.high();
		Thread.sleep(1000);
		leftForward.low();
		rightForward.low();

		gpio.shutdown();
		System.out.println("END");
	}
}
