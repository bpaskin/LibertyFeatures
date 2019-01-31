package com.ibm.liberty.feature.status.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ibm.liberty.feature.status.beans.Status;

@Path("/")
public class ServerStatus {
	
	private static String CLASSNAME = Status.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public StatusResponse getStatus() {
		LOGGER.entering(CLASSNAME, "getStatus");

		Status status = new Status();
		StatusResponse response = new StatusResponse();
		
		LOGGER.fine("Before getHostname");
		response.setSystemHostname(status.getHostname());
		LOGGER.fine("Before getServerInfo");
		response.setServerInfo(status.getServerInfo());
		LOGGER.fine("Before getApplications");
		response.setApplication(status.getApplications());
		LOGGER.fine("Before getEndpoints");
		response.setEndpoint(status.getEndpoints());
		LOGGER.fine("Before getserverConfigFiles");
		response.setConfigFile(status.getserverConfigFiles());
		LOGGER.fine("Before getEndpointInfo");
		response.setEndpointInfo(status.getEndpointInfo());
		
		LOGGER.exiting(CLASSNAME, "getStatus");
		return response;
	}
}
