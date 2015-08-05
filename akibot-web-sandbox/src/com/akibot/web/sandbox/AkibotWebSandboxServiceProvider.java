package com.akibot.web.sandbox;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;


@Path("services/mybean")
public class AkibotWebSandboxServiceProvider {

	@Context
	private UriInfo uriInfo;
	@Context
	private Request request;


	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<MyBean> get() {
		System.out.println("Started...");
		List<MyBean> myBeanList = new ArrayList<MyBean>();
		myBeanList.add(new MyBean(1, "Michael"));
		myBeanList.add(new MyBean(2, "Jimmy"));
		myBeanList.add(new MyBean(3, "Nick"));
		System.out.println("finished.");
		return myBeanList;

	}

}
