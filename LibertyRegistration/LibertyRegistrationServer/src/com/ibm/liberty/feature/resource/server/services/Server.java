package com.ibm.liberty.feature.resource.server.services;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.liberty.feature.resource.beans.AppServerInfo;
import com.ibm.liberty.feature.resource.entities.ServerInfo;
import com.ibm.liberty.feature.resource.entities.ServerInfoPK;

/**
 * This is the Server component of the Liberty Registration Client
 * 
 * requires the features in the <featureManager>
 *     <featureManager>
 *     	<feature>jaxrs-2.0</feature>
 *     	<feature>jpa-2.1</feature>
 *    	<feature>ejbLite-3.2</feature>
 *     </featureManager>
 *     
 * A datasource is required with the jndiName="jdbc/LibertyRegistration" 
 * 
 * @author Brian S Paskin (IBM R&D Support Services)
 * @version 1.0.0.0 (15/01/2018)
 *
 */

@Stateless
@Path("server")
public class Server {
	
	private static String CLASSNAME = Server.class.getName();
	private static Logger LOGGER = Logger.getLogger(CLASSNAME);
	
	@PersistenceContext 
	EntityManager em;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getData(AppServerInfo server) {
		LOGGER.entering(CLASSNAME, "getData", server);
		addServer(server);
		LOGGER.exiting(CLASSNAME, "getData",  Response.ok());
		return Response.ok().build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ServerInfo> listServers() {
		LOGGER.entering(CLASSNAME, "listServers");
		LOGGER.exiting(CLASSNAME, "listServers");
		return em.createNamedQuery("ServerInfo.findAll", ServerInfo.class).getResultList();
	}
	
	@Transactional
	public void addServer(AppServerInfo server) {
		LOGGER.entering(CLASSNAME, "addServer", server);

		ServerInfo serverInfo = new ServerInfo();
		ServerInfoPK serverInfoPK = new ServerInfoPK();
		
		serverInfoPK.setHostname(server.getHostname());
		serverInfoPK.setServerName(server.getServerName());
		serverInfo.setId(serverInfoPK);
		serverInfo.setJdkVersion(server.getJdkVersion());
		serverInfo.setJreVersion(server.getJreVersion());
		serverInfo.setLibertyVersion(server.getLibertyVersion());
		
		LOGGER.fine("Finding original entry");
		ServerInfo old = em.find(ServerInfo.class, serverInfoPK);
		if (null == old) {
			LOGGER.fine("Adding entry to database");
			em.persist(serverInfo);
		} else {
			LOGGER.fine("Updating database entry");
			em.merge(serverInfo);
		}
		em.flush();
		em.clear();
		
		LOGGER.exiting(CLASSNAME, "addServer");
	}	
}
