package com.akibot.web.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.akibot.web.bean.BeanUtils;
import com.akibot.web.bean.SimplifiedClientDescription;
import com.akibot.web.listener.AkiBotWebMaster;

@Path("services/clientmonitoring")
public class ClientMonitoringServiceProvider {
	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<SimplifiedClientDescription> getList() {
		System.out.println("ClientMonitoringServiceProvider.getList");
		return BeanUtils.simplifyClientDescriptionList(AkiBotWebMaster.getAkibotWebClient().getClientDescriptionList());
	}
}
