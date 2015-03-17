package com.akibot.tanktrack.component.tanktrack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine2.message.Message;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class DD1TankTrackComponent extends TankTrackComponent {
	static final Logger log = LogManager.getLogger(DD1TankTrackComponent.class.getName());
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
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof StickMotionRequest) {
			StickMotionRequest request = (StickMotionRequest) message;

			switch (request.getDirectionType()) {
			case FORWARD:
				log.debug(this.getAkibotClient() + ": FORWARD");
				defaultState();
				rightForwardPin.high();
				leftForwardPin.high();
				break;
			case BACKWARD:
				log.debug(this.getAkibotClient() + ": BACKWARD");
				defaultState();
				rightBackwardPin.high();
				leftBackwardPin.high();
				break;
			case LEFT:
				log.debug(this.getAkibotClient() + ": LEFT");
				defaultState();
				rightForwardPin.high();
				leftBackwardPin.high();
				break;
			case RIGHT:
				log.debug(this.getAkibotClient() + ": RIGHT");
				defaultState();
				leftForwardPin.high();
				rightBackwardPin.high();
				break;
			default:
				log.debug(this.getAkibotClient() + ": STOP");
				defaultState();
				break;
			}
		}
	}

	@Override
	public void start() {
		log.debug(this.getAkibotClient() + ": Initializing Tanktrack GPIOs");
		gpio = GpioFactory.getInstance();

		rightBackwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "rightIA", PinState.LOW);
		rightForwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "rightIB", PinState.LOW);

		leftBackwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "leftIA", PinState.LOW);
		leftForwardPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "leftIB", PinState.LOW);

		defaultState();

		log.debug(this.getAkibotClient() + ": TankTrack GPIOs initialized successfully");
	}

}
