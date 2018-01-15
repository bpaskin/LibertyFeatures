package com.ibm.liberty.feature.resource.beans;

public class AppServerInfo {

	private String hostname;
	private String serverName;
	private String libertyVersion;
	private String jdkVersion;
	private String jreVersion;
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getLibertyVersion() {
		return libertyVersion;
	}
	public void setLibertyVersion(String libertyVersion) {
		this.libertyVersion = libertyVersion;
	}
	public String getJdkVersion() {
		return jdkVersion;
	}
	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}
	public String getJreVersion() {
		return jreVersion;
	}
	public void setJreVersion(String jreVersion) {
		this.jreVersion = jreVersion;
	}
}
