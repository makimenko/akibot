package com.akibot.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.akibot.engine2.network.ClientDescription;
import com.akibot.web.bean.SimplifiedClientDescription;
import com.akibot.web.listener.AkiBotWebMaster;

@Path("services/ClientMonitoring")
public class ClientMonitoringServiceProvider {
	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<SimplifiedClientDescription> get() {
		System.out.println("ClientMonitoringServiceProvider Started...");
		List<SimplifiedClientDescription> result = new ArrayList<SimplifiedClientDescription>();

		// Convert to simplified
		List<ClientDescription> list = AkiBotWebMaster.getAkibotWebClient().getClientDescriptionList();
		for (ClientDescription descr : list) {
			SimplifiedClientDescription simplifiedClientDescription = new SimplifiedClientDescription();
			simplifiedClientDescription.setName(descr.getName());
			result.add(simplifiedClientDescription);
		}
		System.out.println("ClientMonitoringServiceProvider finished ("+result.size()+")");
		return result;
	}
}
