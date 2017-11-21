package com.ibm.liberty.feature.status.beans;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * Backing Bean for Liberty to show some basic information of the App Server 
 * 
 * @author Brian S Paskin (IBM R&D Support Services)
 * @version 1.1.0.0 (28/09/2017)
 *
 */

@ManagedBean
@ApplicationScoped
public class Status {
	
	private String appSeverName;
	private String libertyVersion;
	private String jdkVersion;
	private String jreVersion;
	private String hostname;
	private List<Application> applications;
	
	public Status () {
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		
		try {
			ObjectName mbean = new ObjectName("WebSphere:feature=kernel,name=ServerInfo");
			if (mbeanServer.isRegistered(mbean)) {
				appSeverName = (String) mbeanServer.getAttribute(mbean, "Name");
				libertyVersion = (String) mbeanServer.getAttribute(mbean, "LibertyVersion");
				jdkVersion = (String) mbeanServer.getAttribute(mbean, "JavaSpecVersion");
				jreVersion = (String) mbeanServer.getAttribute(mbean, "JavaRuntimeVersion");	
			} else {
				appSeverName = ManagementFactory.getRuntimeMXBean().getName();
			}		
		} catch (MalformedObjectNameException | ReflectionException | InstanceNotFoundException | MBeanException | AttributeNotFoundException e) {
			e.printStackTrace(System.err);
		}
		
		try {
			hostname = InetAddress.getLocalHost().getHostName(); 
		} catch (UnknownHostException e) {
			e.printStackTrace(System.err);
			hostname = "Unknown";
		}
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getAppSeverName() {
		return appSeverName;
	}
	
	public String getAccessTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public String getLibertyVersion() {
		return libertyVersion;
	}

	public String getJdkVersion() {
		return jdkVersion;
	}

	public String getJreVersion() {
		return jreVersion;
	}

	public List<Application> getApplications() {
		applications = new ArrayList<Application>(); 
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		
		try {
			ObjectName mbeans = new ObjectName("WebSphere:service=com.ibm.websphere.application.ApplicationMBean,name=*");
			Set<ObjectName> appMBeans = new HashSet<ObjectName>();
			appMBeans.addAll(mbeanServer.queryNames(mbeans, null));
			
			for (ObjectName mbean : appMBeans) {
				Application app = new Application();
				app.setName(mbean.getKeyProperty("name"));
				app.setState((String) mbeanServer.getAttribute(mbean, "State"));
				applications.add(app);
			} 
		} catch (MalformedObjectNameException | AttributeNotFoundException | MBeanException | ReflectionException | InstanceNotFoundException e) {
				e.printStackTrace(System.err);
		}
		return applications;	
	}
}
