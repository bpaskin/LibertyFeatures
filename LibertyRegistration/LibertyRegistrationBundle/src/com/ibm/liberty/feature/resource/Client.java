package com.ibm.liberty.feature.resource;

import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Client to acquire the nessary information and send the data in JSON
 * format to the server.
 * 
 * @author Brian S Paskin (IBM R&D Support Services)
 * @version 1.0.0.0 (15/01/2018)
 *
 */

public class Client {
	
	private static String CLASSNAME = Client.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);

	public void sendInformation(String hostname, String port, String protocol) {
		LOGGER.entering(CLASSNAME, "sendInformation");

		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		AppServerInfo info = new AppServerInfo();
		
		try {
			ObjectName mbean = new ObjectName("WebSphere:feature=kernel,name=ServerInfo");
			
			if (mbeanServer.isRegistered(mbean)) {
				info.setServerName((String) mbeanServer.getAttribute(mbean, "Name"));
				info.setLibertyVersion((String) mbeanServer.getAttribute(mbean, "LibertyVersion"));
				info.setJdkVersion((String) mbeanServer.getAttribute(mbean, "JavaSpecVersion"));
				info.setJreVersion((String) mbeanServer.getAttribute(mbean, "JavaRuntimeVersion"));	
				info.setHostname(InetAddress.getLocalHost().getHostName()); 
			}
			
			String json ="{" + "\"hostname\":\"" + info.getHostname() +  "\"" + 
					",\"serverName\":\"" + info.getServerName() +  "\"" + 
					",\"libertyVersion\":\"" + info.getLibertyVersion() +  "\"" + 
					",\"jdkVersion\":\"" + info.getJdkVersion() +  "\"" + 
					",\"jreVersion\":\"" + info.getJreVersion() +  "\"" + "}";
			
			LOGGER.fine(json);
			
			byte[] out = json.getBytes(StandardCharsets.UTF_8);

			URL url = new URL(protocol + "://" + hostname + ":" + port + "/LibertyRegistrationServer/services/server");
			
			LOGGER.fine(url.toString());
			
			URLConnection connection = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) connection;
			http.setRequestMethod("POST");
			http.setReadTimeout(3000);
			http.setDoOutput(true);
			http.setConnectTimeout(3000);
			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			http.setFixedLengthStreamingMode(out.length);
			http.connect();
			OutputStream os = http.getOutputStream();
			os.write(out);
			os.flush();
			os.close();
			
			if (200 != http.getResponseCode()) {
				throw new Exception("Return code from JSON call was " + http.getResponseCode());
			}
			
			http.disconnect();
			
			LOGGER.exiting(CLASSNAME, "sendInformation");
		} catch (Exception e) {
			LOGGER.throwing(CLASSNAME, "sendInformation", e);
		}
	}
}
