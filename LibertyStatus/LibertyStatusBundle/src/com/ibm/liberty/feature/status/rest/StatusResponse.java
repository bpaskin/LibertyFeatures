package com.ibm.liberty.feature.status.rest;

import java.io.Serializable;
import java.util.List;

import com.ibm.liberty.feature.status.beans.Application;
import com.ibm.liberty.feature.status.beans.EndpointInfo;
import com.ibm.liberty.feature.status.beans.ServerInfo;

public class StatusResponse implements Serializable {	
	private static final long serialVersionUID = 1994397279389436324L;
	
	private String systemHostname;
	private ServerInfo serverInfo;
	private List<Application> application;
	private List<String> endpoint;
	private List<String> configFile;
	private List<EndpointInfo> endpointInfo;

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	public List<Application> getApplication() {
		return application;
	}

	public void setApplication(List<Application> application) {
		this.application = application;
	}

	public List<String> getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(List<String> endpoint) {
		this.endpoint = endpoint;
	}

	public List<String> getConfigFile() {
		return configFile;
	}

	public void setConfigFile(List<String> configFile) {
		this.configFile = configFile;
	}

	public String getSystemHostname() {
		return systemHostname;
	}

	public void setSystemHostname(String systemHostname) {
		this.systemHostname = systemHostname;
	}

	public List<EndpointInfo> getEndpointInfo() {
		return endpointInfo;
	}

	public void setEndpointInfo(List<EndpointInfo> endpointInfo) {
		this.endpointInfo = endpointInfo;
	}
	
}
