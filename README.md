# LibertyFeatures
Features created for IBM Liberty

LibertyStatus.esa
Displays information about the Application Server and status of the applications that are deployed.
Includes hostname, App Server name, access time, Liberty version number and Java version and release 
The application names are listed and their current status

To install wlp/bin/installUtility LibertyStatus.esa --to=usr

Include in server.xml in the <featureManager>: <feature>usr:LibertyStatus</feature>
 
Access via : http://hostname:port/LibertyStatus
