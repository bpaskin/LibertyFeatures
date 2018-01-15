package com.ibm.liberty.feature.resource.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ServerInfo database table.
 * 
 */
@Embeddable
public class ServerInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String hostname;

	private String serverName;

	public ServerInfoPK() {
	}
	public String getHostname() {
		return this.hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getServerName() {
		return this.serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ServerInfoPK)) {
			return false;
		}
		ServerInfoPK castOther = (ServerInfoPK)other;
		return 
			this.hostname.equals(castOther.hostname)
			&& this.serverName.equals(castOther.serverName);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.hostname.hashCode();
		hash = hash * prime + this.serverName.hashCode();
		
		return hash;
	}
}