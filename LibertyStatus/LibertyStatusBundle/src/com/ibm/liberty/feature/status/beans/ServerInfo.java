package com.ibm.liberty.feature.status.beans;

import java.io.Serializable;

public class ServerInfo implements Serializable {
	private static final long serialVersionUID = -5453044366770492984L;

	private String defaultHostname;
	private String installDirectory;
	private String javaRuntimeVersion;
	private String javaSpecVersion;
	private String libertyVersion;
	private String serverName;
	private String userDirectory;
	
	public String getDefaultHostname() {
		return defaultHostname;
	}
	public void setDefaultHostname(String defaultHostname) {
		this.defaultHostname = defaultHostname;
	}
	public String getInstallDirectory() {
		return installDirectory;
	}
	public void setInstallDirectory(String installDirectory) {
		this.installDirectory = installDirectory;
	}
	public String getJavaRuntimeVersion() {
		return javaRuntimeVersion;
	}
	public void setJavaRuntimeVersion(String javaRuntimeVersion) {
		this.javaRuntimeVersion = javaRuntimeVersion;
	}
	public String getJavaSpecVersion() {
		return javaSpecVersion;
	}
	public void setJavaSpecVersion(String javaSpecVersion) {
		this.javaSpecVersion = javaSpecVersion;
	}
	public String getLibertyVersion() {
		return libertyVersion;
	}
	public void setLibertyVersion(String libertyVersion) {
		this.libertyVersion = libertyVersion;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getUserDirectory() {
		return userDirectory;
	}
	public void setUserDirectory(String userDirectory) {
		this.userDirectory = userDirectory;
	}
}
