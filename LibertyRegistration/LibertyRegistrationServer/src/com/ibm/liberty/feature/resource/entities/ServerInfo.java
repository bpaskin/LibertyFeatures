package com.ibm.liberty.feature.resource.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ServerInfo database table.
 * 
 */
@Entity
@NamedQuery(name="ServerInfo.findAll", query="SELECT s FROM ServerInfo s")
@Table(name="ServerInfo")
public class ServerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ServerInfoPK id;

	private String jdkVersion;

	private String jreVersion;

	private String libertyVersion;

	public ServerInfo() {
	}

	public ServerInfoPK getId() {
		return this.id;
	}

	public void setId(ServerInfoPK id) {
		this.id = id;
	}

	public String getJdkVersion() {
		return this.jdkVersion;
	}

	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}

	public String getJreVersion() {
		return this.jreVersion;
	}

	public void setJreVersion(String jreVersion) {
		this.jreVersion = jreVersion;
	}

	public String getLibertyVersion() {
		return this.libertyVersion;
	}

	public void setLibertyVersion(String libertyVersion) {
		this.libertyVersion = libertyVersion;
	}

}