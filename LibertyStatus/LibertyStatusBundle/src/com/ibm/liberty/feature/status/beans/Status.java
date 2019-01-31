package com.ibm.liberty.feature.status.beans;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.ibm.websphere.application.ApplicationMBean;
import com.ibm.websphere.config.mbeans.ServerXMLConfigurationMBean;
import com.ibm.websphere.endpoint.EndPointInfoMBean;
import com.ibm.websphere.kernel.server.ServerEndpointControlMBean;
import com.ibm.websphere.kernel.server.ServerInfoMBean;
import com.ibm.wsspi.kernel.service.location.WsLocationAdmin;

/**
 * Backing Bean for Liberty to show some basic information of the App Server 
 * 
 * @author Brian S Paskin (IBM R&D)
 * @version 1.2.0.0 (09/05/2018)
 *
 * DATE  
 * 09/05/2018 BSP Added Start/Stop functionality, Logging
 * 20/01/2019 BSP refactored code, added new mbeans, added beans for certain mbeans
 */

@ManagedBean 
@ApplicationScoped
public class Status {
	
	private String hostname;
	MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
	private static String CLASSNAME = Status.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	public Status () {		
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
	
	public ServerInfo getServerInfo() {
		ServerInfo serverInfo = new ServerInfo();
		
		try {
			LOGGER.fine("Before gettings Application MBean");
			ObjectName mbeans = new ObjectName("WebSphere:feature=kernel,name=ServerInfo");
			LOGGER.fine("Getting appMbeans object Names");
			
			if (mbeanServer.isRegistered(mbeans)) {
				LOGGER.fine("MBean is registered");
				ServerInfoMBean serverInfoMBean = JMX.newMBeanProxy(mbeanServer, mbeans, ServerInfoMBean.class);

				serverInfo.setDefaultHostname(serverInfoMBean.getDefaultHostname());
				serverInfo.setInstallDirectory(serverInfoMBean.getInstallDirectory());
				serverInfo.setJavaRuntimeVersion(serverInfoMBean.getJavaRuntimeVersion());
				serverInfo.setJavaSpecVersion(serverInfoMBean.getJavaSpecVersion());
				serverInfo.setLibertyVersion(serverInfoMBean.getLibertyVersion());
				serverInfo.setServerName(serverInfoMBean.getName());
				serverInfo.setUserDirectory(serverInfoMBean.getUserDirectory());
			}
		} catch (MalformedObjectNameException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		return serverInfo;
	}
	
	public String getHostname() {
		LOGGER.entering(CLASSNAME, "getHostname");
		return hostname;
	}
	
	
	public String getAccessTime() {
		LOGGER.entering(CLASSNAME, "getAccessTime");
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	public List<Application> getApplications() {
		LOGGER.entering(CLASSNAME, "getApplications");
		List<Application> applications = new ArrayList<Application>(); 
		
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
	
	public List<String> getEndpoints() {
		LOGGER.entering(CLASSNAME, "getEndpoints");
		List<String> endpoints = new ArrayList<String>();
		
		// Make sure that it is 18.0.0.1
		LOGGER.fine("Checking that version is correct to execute method");
		String version = getServerInfo().getLibertyVersion().replaceAll("\\.+", "");
		if (Integer.parseInt(version) < 18001) {
			LOGGER.fine("Version is incorrect: " + version + " < 18001");
			return endpoints;
		}

		try {
			LOGGER.fine("Before getting ServerEndpointControl MBean");
			ObjectName mbean = new ObjectName("WebSphere:feature=kernel,name=ServerEndpointControl");
			LOGGER.fine("After getting ServerEndpointControl MBean");

			if (mbeanServer.isRegistered(mbean)) {
				LOGGER.fine("MBean is registered");
				ServerEndpointControlMBean endpoint = JMX.newMBeanProxy(mbeanServer, mbean, ServerEndpointControlMBean.class);
				endpoints = endpoint.listEndpoints();
			}
		} catch (MalformedObjectNameException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		return endpoints;
	}
	
	
	public List<EndpointInfo> getEndpointInfo() {
		LOGGER.entering(CLASSNAME, "getEndpointInfo");
		List<EndpointInfo> endpointInfos = new ArrayList<EndpointInfo>();

		try {	
			LOGGER.fine("Before getting endpoint MBean");
			ObjectName mbean = new ObjectName("WebSphere:feature=channelfw,type=endpoint,*");
			Set<ObjectInstance> instances = mbeanServer.queryMBeans(mbean, null);

			Iterator<ObjectInstance> iterator = instances.iterator();			         
			while (iterator.hasNext()) {
				ObjectInstance instance = iterator.next();	
				LOGGER.fine("Before getting EndPointInfoMBean MBean");
				mbean = new ObjectName(instance.getObjectName().getCanonicalName());
				LOGGER.fine("After getting EndPointInfoMBean MBean");
				if (mbeanServer.isRegistered(mbean)) {
					LOGGER.fine("MBean is registered");
					EndPointInfoMBean endpoint = JMX.newMBeanProxy(mbeanServer, mbean, EndPointInfoMBean.class);
					
					LOGGER.fine("Adding endpoint to the Bean");
					EndpointInfo endpointInfo = new EndpointInfo();
					endpointInfo.setHost(endpoint.getHost());
					endpointInfo.setName(endpoint.getName());
					endpointInfo.setPort(endpoint.getPort());
					endpointInfos.add(endpointInfo);
					LOGGER.fine("After adding endpoint to the Bean");
				}
			}
		} catch (MalformedObjectNameException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace(System.err);
		}
		return endpointInfos;
	}
	
	public List<String> getserverConfigFiles() {
		LOGGER.entering(CLASSNAME, "getFeatureList");
		List<String> serverConfigFiles = new ArrayList<String>();

	try {	
		LOGGER.fine("Before getting FeatureList MBean");
		ObjectName mbean = new ObjectName("WebSphere:name=com.ibm.websphere.config.mbeans.ServerXMLConfigurationMBean");
		LOGGER.fine("After getting FeatureList MBean");

		if (mbeanServer.isRegistered(mbean)) {
			LOGGER.fine("MBean is registered");
			ServerXMLConfigurationMBean configs = JMX.newMBeanProxy(mbeanServer, mbean, ServerXMLConfigurationMBean.class);		
			List<String> configFiles = new ArrayList<String>(configs.fetchConfigurationFilePaths());

			LOGGER.fine("Loop through config files");
			Pattern pattern = Pattern.compile("\\$.*?\\}");
			for (String s: configFiles) {
				
				LOGGER.fine("Matching pattern of string");
				Matcher m = pattern.matcher(s);
				while (m.find()) {
					LOGGER.fine("finding the substring");
				    String alias = m.group(0);
					LOGGER.fine("looking up the alias : " + alias);
					BundleContext context =  FrameworkUtil.getBundle(Status.class).getBundleContext();
					ServiceReference<WsLocationAdmin> locationAdminRef = context.getServiceReference(WsLocationAdmin.class);
					WsLocationAdmin locationAdmin = context.getService(locationAdminRef);
					String fullPath = locationAdmin.resolveString(alias);
					fullPath = fullPath + s.substring(alias.length()+1);
					serverConfigFiles.add(fullPath);
					LOGGER.fine("path found and returning " + fullPath);
				}
			}
		}
	} catch (MalformedObjectNameException e) {
		LOGGER.severe(e.getMessage());
		e.printStackTrace(System.err);
	}
		return serverConfigFiles;
	}
}
