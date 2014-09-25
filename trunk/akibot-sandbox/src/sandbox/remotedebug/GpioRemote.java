package sandbox.remotedebug;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioRemote {
	public static void main(String[] args) throws Exception {
		System.out.println("<--Pi4J--> GPIO Control Example ... started.");
		final GpioController gpio = GpioFactory.getInstance();
		System.out.println("<--Pi4J--> GPIO Factory initiated.");
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(
				RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
		System.out.println("--> GPIO state should be: ON");
		Thread.sleep(5000);
		pin.low();
		System.out.println("--> GPIO state should be: OFF");
		Thread.sleep(5000);
		pin.toggle();
		System.out.println("--> GPIO state should be: ON");
		Thread.sleep(5000);
		pin.toggle();
		System.out.println("--> GPIO state should be: OFF");
		Thread.sleep(5000);
		System.out.println("--> GPIO state should be: ON for only 1 second");
		pin.pulse(1000, true);
		gpio.shutdown();
		System.out.println(" Finishing");
	}
}