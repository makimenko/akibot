package com.akibot.tanktrack.component.tanktrack;

import com.akibot.engine2.exception.FailedToStartException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class DD1TankTrackComponent extends TankTrackComponent {
	static final AkiLogger log = AkiLogger.create(DD1TankTrackComponent.class);
	private GpioController gpio;
	private GpioPinDigitalOutput leftBackwardPin;
	private GpioPinDigitalOutput leftForwardPin;
	private GpioPinDigitalOutput rightBackwardPin;
	private GpioPinDigitalOutput rightForwardPin;
	private Pin rightIApin;
	private Pin rightIBpin;
	private Pin leftIApin;
	private Pin leftIBpin;

	public DD1TankTrackComponent(Pin rightIApin, Pin rightIBpin, Pin leftIApin, Pin leftIBpin) {
		this.rightIApin = rightIApin;
		this.rightIBpin = rightIBpin;
		this.leftIApin = leftIApin;
		this.leftIBpin = leftIBpin;
	}

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
			log.debug(this.getAkibotClient() + ": " + request);
			switch (request.getDirectionType()) {
			case FORWARD:
				defaultState();
				rightForwardPin.high();
				leftForwardPin.high();
				break;
			case BACKWARD:
				defaultState();
				rightBackwardPin.high();
				leftBackwardPin.high();
				break;
			case LEFT:
				defaultState();
				rightForwardPin.high();
				leftBackwardPin.high();
				break;
			case RIGHT:
				defaultState();
				leftForwardPin.high();
				rightBackwardPin.high();
				break;
			default:
				defaultState();
				break;
			}
			StickMotionResponse response = new StickMotionResponse();
			response.copySyncId(message);
			this.getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		} else if (message instanceof TimedMotionRequest) {
			TimedMotionRequest timedMotionRequest = (TimedMotionRequest) message;
			log.debug(this.getAkibotClient() + ": " + timedMotionRequest);

			defaultState();
			switch (timedMotionRequest.getDirectionType()) {
			case FORWARD:
				rightForwardPin.high();
				leftForwardPin.high();
				break;
			case BACKWARD:
				rightBackwardPin.high();
				leftBackwardPin.high();
				break;
			case LEFT:
				rightForwardPin.high();
				leftBackwardPin.high();
				break;
			case RIGHT:
				leftForwardPin.high();
				rightBackwardPin.high();
				break;
			}
			Thread.sleep(timedMotionRequest.getMilliseconds());
			defaultState();

			TimedMotionResponse response = new TimedMotionResponse();
			response.copySyncId(message);
			this.getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);

		}
	}

	@Override
	public void start() throws FailedToStartException {
		try {
			log.debug(this.getAkibotClient() + ": Initializing Tanktrack GPIOs");
			gpio = GpioFactory.getInstance();

			rightBackwardPin = gpio.provisionDigitalOutputPin(rightIApin, "rightIA", PinState.LOW);
			rightForwardPin = gpio.provisionDigitalOutputPin(rightIBpin, "rightIB", PinState.LOW);

			leftBackwardPin = gpio.provisionDigitalOutputPin(leftIApin, "leftIA", PinState.LOW);
			leftForwardPin = gpio.provisionDigitalOutputPin(leftIBpin, "leftIB", PinState.LOW);

			defaultState();

			log.debug(this.getAkibotClient() + ": TankTrack GPIOs initialized successfully");
		} catch (Exception e) {
			throw new FailedToStartException(e);
		}
	}

}
