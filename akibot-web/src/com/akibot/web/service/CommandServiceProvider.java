package com.akibot.web.service;

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
import com.akibot.tanktrack.component.orientation.OrientationRequest;
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

	@PUT
	@Path("/testRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response testRequest(JAXBElement<TestRequest> testRequest) {
		return broadcastRequest(testRequest);
	}

	@PUT
	@Path("/orientationRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response orientationRequest(JAXBElement<OrientationRequest> orientationRequest) {
		return broadcastRequest(orientationRequest);
	}

	@PUT
	@Path("/stickMotionRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response stickMotionRequest(JAXBElement<StickMotionRequest> stickMotionRequest) {
		return broadcastRequest(stickMotionRequest);
	}

	@PUT
	@Path("/timedMotionRequest")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response timedMotionRequest(JAXBElement<TimedMotionRequest> timedMotionRequest) {
		return broadcastRequest(timedMotionRequest);
	}

	public Response broadcastRequest(JAXBElement request) {
		log.debug("broadcastRequest: " + request.getValue());
		try {
			AkiBotWebMaster.getAkibotWebComponent().broadcastMessage((Message) request.getValue());
		} catch (FailedToSendMessageException e) {
			log.catching(e);
			return Response.serverError().build();
		}
		return Response.created(uriInfo.getAbsolutePath()).build();
	}

}
