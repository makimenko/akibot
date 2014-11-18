package sandbox.pi4j.distance;

import java.io.IOException;

import sandbox.pi4j.distance.DistanceMonitor.TimeoutException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class DistanceHYSRF05 {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting...");
		final GpioController gpio = GpioFactory.getInstance();
		System.out.println("Instance initialized.");

		GpioPinDigitalOutput triggerPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "HYSRF05_Trigger", PinState.LOW);
		GpioPinDigitalInput echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, PinPullResistance.PULL_DOWN);

		// Pin gpio23_trigger = RaspiPin.GPIO_23;
		// Pin gpio24_echo = RaspiPin.GPIO_24;
		DistanceCalculation distanceCalculation = new DistanceCalculation(triggerPin, echoPin);
		// DistanceMonitor distanceMonitor = new DistanceMonitor(gpio24_echo,
		// gpio23_trigger);

		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start <= 120000) {
			double distance;
			distance = distanceCalculation.getDistance();

			System.out.format("DISTANCE: %10.3f\n", distance);

			Thread.sleep(200);
		}

		gpio.shutdown();
		System.out.println("END");
	}

}
