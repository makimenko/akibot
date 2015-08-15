package com.akibot.web.service;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.akibot.engine2.component.test.TestRequest;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.distance.DistanceRequest;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.orientation.OrientationRequest;
import com.akibot.tanktrack.component.servo.ServoRequest;
import com.akibot.tanktrack.component.speech.synthesis.SpeechSynthesisRequest;
import com.akibot.tanktrack.component.tanktrack.StickMotionRequest;
import com.akibot.tanktrack.component.tanktrack.TimedMotionRequest;
import com.akibot.web.listener.AkiBotWebMaster;

@Path("services/control")
public class CommandServiceProvider {
	static final AkiLogger log = AkiLogger.create(CommandServiceProvider.class);

	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	private Response broadcastJAX(JAXBElement request) {
		return broadcastMessage((Message) request.getValue());
	}

	private Response broadcastMessage(Message requestMessage) {
		log.debug("broadcastRequestMessage: " + requestMessage);
		try {
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage((Message) requestMessage);
		} catch (FailedToSendMessageException e) {
			log.catching(e);
			return Response.serverError().build();
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}

	@PUT
	@Path("/testRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response testRequest(JAXBElement<TestRequest> testRequest) {
		return broadcastJAX(testRequest);
	}

	@PUT
	@Path("/orientationRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response orientationRequest(JAXBElement<OrientationRequest> orientationRequest) {
		return broadcastJAX(orientationRequest);
	}

	@PUT
	@Path("/stickMotionRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response stickMotionRequest(JAXBElement<StickMotionRequest> stickMotionRequest) {
		return broadcastJAX(stickMotionRequest);
	}

	@PUT
	@Path("/timedMotionRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response timedMotionRequest(JAXBElement<TimedMotionRequest> timedMotionRequest) {
		return broadcastJAX(timedMotionRequest);
	}

	@PUT
	@Path("/speechSynthesisRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response speechSynthesisRequest(JAXBElement<SpeechSynthesisRequest> speechSynthesisRequest) throws UnsupportedEncodingException {
		return broadcastJAX(speechSynthesisRequest);
	}

	@PUT
	@Path("/distanceRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response distanceRequest() {
		return broadcastMessage(new DistanceRequest());
	}

	@PUT
	@Path("/gyroscopeValueRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response gyroscopeValueRequest() {
		return broadcastMessage(new GyroscopeValueRequest());
	}

	@PUT
	@Path("/servoCenterRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response servoCenterRequest() {
		log.debug("servoCenterRequest");
		try {
			ServoRequest servoFrontBaseRequest = new ServoRequest();
			servoFrontBaseRequest.setMicroseconds(500000);
			servoFrontBaseRequest.setValue(14);

			ServoRequest servoFrontHeadRequest = new ServoRequest();
			servoFrontHeadRequest.setMicroseconds(500000);
			servoFrontHeadRequest.setValue(14);

			servoFrontBaseRequest.setTo("akibot.servo.back.base");
			servoFrontHeadRequest.setTo("akibot.servo.back.head");
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage(servoFrontBaseRequest);
			Thread.sleep(1000);
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage(servoFrontHeadRequest);
			Thread.sleep(1000);

			servoFrontBaseRequest.setTo("akibot.servo.front.base");
			servoFrontHeadRequest.setTo("akibot.servo.front.head");
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage(servoFrontBaseRequest);
			Thread.sleep(1000);
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage(servoFrontHeadRequest);
			Thread.sleep(1000);

		} catch (FailedToSendMessageException e1) {
			log.catching(e1);
		} catch (InterruptedException e1) {
			log.catching(e1);
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}

}
