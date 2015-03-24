package com.akibot.tanktrack.component.servo;

import akibot.jni.java.AkibotJniLibrary;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;

public class ServoComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ServoComponent.class);
	private AkibotJniLibrary lib;
	private int servoPin;
	private int initialValue;
	private int pwmRange;
	private int divisor;

	public ServoComponent(int servoPin, int initialValue, int pwmRange, int divisor) {
		this.lib = new AkibotJniLibrary();
		this.lib.initialize();
		this.servoPin = servoPin;
		this.initialValue = initialValue;
		this.pwmRange = pwmRange;
		this.divisor = divisor;
	}

	public ServoComponent() throws Exception {
		throw new Exception("Unimplemented constructor!");
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ServoRequest) {
			ServoRequest request = (ServoRequest) message;
			ServoResponse response = new ServoResponse();
			this.lib.servo(servoPin, initialValue, pwmRange, divisor, request.getValue(), request.getMicroseconds());
			response.copySyncId(message);
			getAkibotClient().getOutgoingMessageManager().broadcastMessage(response);
		}
	}

	@Override
	public void start() {

	}

}
