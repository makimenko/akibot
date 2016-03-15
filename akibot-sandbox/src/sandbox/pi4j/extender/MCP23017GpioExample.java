package sandbox.pi4j.extender;

import java.io.IOException;

import com.pi4j.gpio.extension.mcp.MCP23017GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP23017Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider for GPIO pin state control and monitoring.
 * </p>
 * 
 * <p>
 * This example implements the MCP23017 GPIO expansion board. More information about the board can be found here: *
 * http://ww1.microchip.com/downloads/en/DeviceDoc/21952b.pdf
 * </p>
 * 
 * <p>
 * The MCP23017 is connected via I2C connection to the Raspberry Pi and provides 16 GPIO pins that can be used for either digital input or digital output pins.
 * </p>
 * 
 * @author Robert Savage
 */
public class MCP23017GpioExample {

	public static void main(String args[]) throws InterruptedException, IOException {

		System.out.println("<--Pi4J--> MCP23017 GPIO Example ... started.");

		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		// create custom MCP23017 GPIO provider
		final MCP23017GpioProvider gpioProvider = new MCP23017GpioProvider(I2CBus.BUS_1, 0x20);

		// provision gpio input pins from MCP23017
		GpioPinDigitalInput myInputs[] = { gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A0, "MyInput-A0", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A1, "MyInput-A1", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A2, "MyInput-A2", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A3, "MyInput-A3", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A4, "MyInput-A4", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A5, "MyInput-A5", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A6, "MyInput-A6", PinPullResistance.PULL_UP),
				gpio.provisionDigitalInputPin(gpioProvider, MCP23017Pin.GPIO_A7, "MyInput-A7", PinPullResistance.PULL_UP), };

		// create and register gpio pin listener
		gpio.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
			}
		}, myInputs);

		// provision gpio output pins and make sure they are all LOW at startup
		GpioPinDigitalOutput myOutputs[] = { gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B0, "MyOutput-B0", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B1, "MyOutput-B1", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B2, "MyOutput-B2", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B3, "MyOutput-B3", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B4, "MyOutput-B4", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B5, "MyOutput-B5", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B6, "MyOutput-B6", PinState.LOW),
				gpio.provisionDigitalOutputPin(gpioProvider, MCP23017Pin.GPIO_B7, "MyOutput-B7", PinState.LOW) };

		// keep program running for 20 seconds
		for (int count = 0; count < 10; count++) {
			System.out.println("true...");
			gpio.setState(true, myOutputs);
			Thread.sleep(1000);
			System.out.println("false...");
			gpio.setState(false, myOutputs);
			Thread.sleep(1000);
		}

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
		gpio.shutdown();
		System.out.println("END.");
	}
}