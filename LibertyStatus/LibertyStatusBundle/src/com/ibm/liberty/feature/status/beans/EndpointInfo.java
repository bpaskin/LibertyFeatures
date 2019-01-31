package com.ibm.liberty.feature.status.beans;

import java.io.Serializable;

public class EndpointInfo implements Serializable {
	private static final long serialVersionUID = 7397450437320958521L;

	private String host;
	private String name;
	private int port;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
