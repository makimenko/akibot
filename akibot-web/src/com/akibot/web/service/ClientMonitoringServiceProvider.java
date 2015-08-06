package com.akibot.web.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.akibot.engine2.network.ClientDescription;
import com.akibot.web.listener.AkiBotWebMaster;

@Path("services/ClientMonitoring")
public class ClientMonitoringServiceProvider {
	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ClientDescription> get() {
		System.out.println("ClientMonitoringServiceProvider Started...");
		return AkiBotWebMaster.getAkibotWebClient().getClientDescriptionList();
	}
}
