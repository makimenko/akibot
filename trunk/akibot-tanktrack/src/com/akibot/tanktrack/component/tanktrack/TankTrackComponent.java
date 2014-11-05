package com.akibot.tanktrack.component.tanktrack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class TankTrackComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(TankTrackComponent.class.getName());
	private GpioController gpio;
	private GpioPinDigitalOutput leftBackwardPin;
	private GpioPinDigitalOutput leftForwardPin;
	private GpioPinDigitalOutput rightBackwardPin;
	private GpioPinDigitalOutput rightForwardPin;

	private void defaultState() {
		if (leftForwardPin.isHigh())
			leftForwardPin.low();

		if (rightForwardPin.isHigh())
			rightForwardPin.low();

		if (leftBackwardPin.isHigh())
			leftBackwardPin.low();

		if (rightBackwardPin.isHigh())
			rightBackwardPin.low();
	}

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			StickMotionRequest request = (StickMotionRequest) message;

			switch (request.getDirectionType()) {
			case FORWARD:
				log.debug("FORWARD");
				defaultState();
				rightForwardPin.high();
				leftForwardPin.high();
				break;
			case BACKWARD:
				log.debug("BACKWARD");
				defaultState();
				rightBackwardPin.high();
				leftBackwardPin.high();
				break;
			case LEFT:
				log.debug("LEFT");
				defaultState();
				rightForwardPin.high();
				leftBackwardPin.high();
				break;
			case RIGHT:
				log.debug("RIGHT");
				defaultState();
				leftForwardPin.high();
				rightBackwardPin.high();
				break;
			default:
				log.debug("STOP");
				defaultState();
				break;
			}
		}
	}

	@Override
	public void run() {
		log.debug("Initializing Tanktrack GPIOs");
		gpio = GpioFactory.getInstance();

		rightBackwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "rightIA", PinState.LOW);
		rightForwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "rightIB", PinState.LOW);

		leftBackwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "leftIA", PinState.LOW);
		leftForwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "leftIB", PinState.LOW);

		defaultState();

		log.debug("TankTrack GPIOs initialized successfully");
	}

}
