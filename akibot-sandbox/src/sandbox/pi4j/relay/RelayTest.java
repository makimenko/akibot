package sandbox.pi4j.relay;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RelayTest {

	private final Pin channelPin = RaspiPin.GPIO_01;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("STARTING...");
		RelayTest relayTest = new RelayTest();
		relayTest.run();
		System.out.println("END.");
	}

	public void run() throws InterruptedException {
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(channelPin, "My Relay", PinState.HIGH);

		System.out.println("waiting");
		Thread.sleep(2000);
		System.out.println("set to low (ON)...");
		pin.low();
		Thread.sleep(2000);
		System.out.println("set to high (OFF)...");
		pin.high();
		Thread.sleep(2000);
		System.out.println("set to low (ON)...");
		pin.low();
		Thread.sleep(2000);
		System.out.println("set to high (OFF)...");
		pin.high();
		Thread.sleep(2000);
	}

}
