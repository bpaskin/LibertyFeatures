package com.ibm.liberty.feature.status.beans;

import java.io.Serializable;

/**
 * Bean for storing Application Information
 * 
 * @author Brian S Paskin (IBM R&D Support Services)
 * @version 1.0.0.0 (09/11/2017)
 *
 */

public class Application implements Serializable {
	private static final long serialVersionUID = 6145920299007986428L;
	private String name;
	private String state; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}