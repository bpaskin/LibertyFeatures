package com.ibm.liberty.feature.resource;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * Activator and Managed Service
 * 
 * Requires that the server.xml includes the libertyRegistration Properties
 * example:
 * <libertyRegistration host="hostname.domain.com" port="9443" protocol="https"/>
 * 
 * @author Brian S Paskin (IBM R&D Support Services)
 * @version 1.0.0.0 (15/01/2018)
 *
 */
public class Activator implements BundleActivator, ManagedService {

	private static String CLASSNAME = Activator.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);

	@Override
	public void start(BundleContext context) throws Exception {
		LOGGER.entering(CLASSNAME, "start", context);
		context.registerService(ManagedService.class, this, getDefaults()).getReference();
		LOGGER.exiting(CLASSNAME, "start");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		LOGGER.entering(CLASSNAME, "stop", context);
		LOGGER.exiting(CLASSNAME, "stop");
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		LOGGER.entering(CLASSNAME, "updated", properties);
		try {
			if (null == properties) {
				throw new RuntimeException("Missing libertyRegistration properties");
			}
			
			if (null == properties.get("protocol") || null == properties.get("port") || null == properties.get("host")) {
				throw new RuntimeException("libertyRegistration requires protocol, hostname and port");
			}
			
			new Client().sendInformation((String) properties.get("host"), (String) properties.get("port"), (String) properties.get("protocol"));
			
		} catch (Exception e) {
			RuntimeException re = new RuntimeException("Failed to retrieve properties", e);
			LOGGER.throwing(CLASSNAME, "updated", re);
			throw re;
		}
		
		LOGGER.exiting(CLASSNAME, "updated");
	}
	
	private static Hashtable<String, ?> getDefaults() {
		LOGGER.entering(CLASSNAME, "getDefaults");
		Hashtable<String, String> defaults = new Hashtable<String, String>();
		defaults.put(org.osgi.framework.Constants.SERVICE_PID, "libertyRegistration");
		LOGGER.exiting(CLASSNAME, "getDefaults");
		return defaults;
	}

}
