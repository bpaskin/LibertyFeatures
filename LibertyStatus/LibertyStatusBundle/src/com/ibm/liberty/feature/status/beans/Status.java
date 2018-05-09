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
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.ibm.websphere.application.ApplicationMBean;

/**
 * Backing Bean for Liberty to show some basic information of the App Server 
 * 
 * @author Brian S Paskin (IBM R&D)
 * @version 1.2.0.0 (09/05/2018)
 *
 * DATE  
 * 09/05/2018 BSP Added Start/Stop functionality, Logging
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
	MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
	
	private static String CLASSNAME = Status.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	public Status () {
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		
		try {
			LOGGER.fine("Before gettings ServerInfo MBean");
			ObjectName mbean = new ObjectName("WebSphere:feature=kernel,name=ServerInfo");
			LOGGER.fine("Afer gettings ServerInfo MBean");

			if (mbeanServer.isRegistered(mbean)) {
				LOGGER.fine("MBean is registered");
				appSeverName = (String) mbeanServer.getAttribute(mbean, "Name");
				LOGGER.fine("appServerName:" + appSeverName);
				libertyVersion = (String) mbeanServer.getAttribute(mbean, "LibertyVersion");
				LOGGER.fine("libertyVersion:" + libertyVersion);
				jdkVersion = (String) mbeanServer.getAttribute(mbean, "JavaSpecVersion");
				LOGGER.fine("jdkVersion:" + jdkVersion);
				jreVersion = (String) mbeanServer.getAttribute(mbean, "JavaRuntimeVersion");
				LOGGER.fine("jreVersion:" + jreVersion);
			} else {
				LOGGER.fine("MBean is not registered");
				appSeverName = ManagementFactory.getRuntimeMXBean().getName();
				LOGGER.fine("appServerName:" + appSeverName);
			}		
		} catch (MalformedObjectNameException | ReflectionException | InstanceNotFoundException | MBeanException | AttributeNotFoundException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		
		try {
			LOGGER.fine("Before hotstname");
			hostname = InetAddress.getLocalHost().getHostName(); 
			LOGGER.fine("hostname:" + hostname);
		} catch (UnknownHostException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
			hostname = "Unknown";
			LOGGER.fine("hostname:" + hostname);
		}
	}
	
	public String getHostname() {
		LOGGER.entering(CLASSNAME, "getHostname");
		return hostname;
	}
	
	public String getAppSeverName() {
		LOGGER.entering(CLASSNAME, "getAppServerName");
		return appSeverName;
	}
	
	public String getAccessTime() {
		LOGGER.entering(CLASSNAME, "getAccessTime");
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public String getLibertyVersion() {
		LOGGER.entering(CLASSNAME, "getLibertyVersion");
		return libertyVersion;
	}

	public String getJdkVersion() {
		LOGGER.entering(CLASSNAME, "getJdkVersion");
		return jdkVersion;
	}

	public String getJreVersion() {
		LOGGER.entering(CLASSNAME, "getJreVersion");
		return jreVersion;
	}

	public List<Application> getApplications() {
		LOGGER.entering(CLASSNAME, "getApplications");
		applications = new ArrayList<Application>(); 
		
		try {
			LOGGER.fine("Before gettings Application MBean");
			ObjectName mbeans = new ObjectName("WebSphere:service=com.ibm.websphere.application.ApplicationMBean,name=*");
			LOGGER.fine("Getting appMbeans object Names");
			Set<ObjectName> appMBeans = new HashSet<ObjectName>();
			LOGGER.fine("Adding all app mbeans to the list");
			appMBeans.addAll(mbeanServer.queryNames(mbeans, null));
			
			LOGGER.fine("Initiate loop for app MBeans");
			for (ObjectName mbean : appMBeans) {
				Application app = new Application();
				app.setName(mbean.getKeyProperty("name"));
				app.setState((String) mbeanServer.getAttribute(mbean, "State"));
				applications.add(app);
				LOGGER.fine("added application to list");
			} 
		} catch (MalformedObjectNameException | AttributeNotFoundException | MBeanException | ReflectionException | InstanceNotFoundException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		LOGGER.exiting(CLASSNAME, "getApplications");
		return applications;	
	}
	
	public String getAppStatus(String name) {
		LOGGER.entering(CLASSNAME, "getAppStatus", name);
		try {
			LOGGER.fine("Before gettings Application MBean");
			ObjectName mbean = new ObjectName("WebSphere:service=com.ibm.websphere.application.ApplicationMBean,name=" + name);
			if (mbeanServer.isRegistered(mbean)) {
				LOGGER.fine("MBean is registered");
				ApplicationMBean app = JMX.newMBeanProxy(mbeanServer, mbean, ApplicationMBean.class);
				LOGGER.exiting(CLASSNAME, "getAppStatus", app.getState());
				return app.getState();
			} 
		} catch (MalformedObjectNameException  e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		LOGGER.fine("MBean is not registered");
		LOGGER.exiting(CLASSNAME, "getAppStatus", "UNKNOWN STATE");
		return "UNKNOWN STATE";
	}
	
	public void startApplication(String name) {
		LOGGER.entering(CLASSNAME, "startApplication", name);
		try {
			LOGGER.fine("Before gettings Application MBean");
			ObjectName mbean = new ObjectName("WebSphere:service=com.ibm.websphere.application.ApplicationMBean,name=" + name);
			if (mbeanServer.isRegistered(mbean)) {
				LOGGER.fine("MBean is registered");
				ApplicationMBean app = JMX.newMBeanProxy(mbeanServer, mbean, ApplicationMBean.class);
				LOGGER.fine("Starting application");
				app.start();
	    		LOGGER.exiting(CLASSNAME, "startApplication");
			}
		} catch (MalformedObjectNameException  e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
			FacesMessage message = new FacesMessage("Error Occurred stopping application");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("errors", message);
		}
	}
	
	public void stopApplication(String name) {
		LOGGER.entering(CLASSNAME, "stopApplication", name);

		try {
			LOGGER.fine("Before gettings Application MBean");
			ObjectName mbean = new ObjectName("WebSphere:service=com.ibm.websphere.application.ApplicationMBean,name=" + name);
			if (mbeanServer.isRegistered(mbean)) {
				LOGGER.fine("MBean is registered");
				ApplicationMBean app = JMX.newMBeanProxy(mbeanServer, mbean, ApplicationMBean.class);
				LOGGER.fine("Stopping application");
				app.stop();
	    		LOGGER.exiting(CLASSNAME, "stopApplication");
			}
		} catch (MalformedObjectNameException  e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
			FacesMessage message = new FacesMessage("Error Occurred stopping application");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("errors", message);
		}
	}
}
