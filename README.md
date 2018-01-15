# Liberty Features
Features created for IBM Liberty

# LibertyStatus
LibertyStatus.esa
Displays information about the Application Server and status of the applications that are deployed.
Includes hostname, App Server name, access time, Liberty version number and Java version and release 
The application names are listed and their current status

To install ```wlp/bin/installUtility install LibertyStatus.esa --to=usr```

Include in server.xml in the ```<featureManager>``` the following feature: ```<feature>usr:LibertyStatus</feature>```
 
Access via : http://hostname:port/LibertyStatus

# LibertyRegistration
Registers Liberty servers with the hostname, servername, version, and java information.  Good for large environments and keeping track of the versions of Liberty that are installed and their current characteristics.

Consists of a User Feature, that should be installed on the Clients and a Server application that can be installed on a central Liberty Server. A database is required.  The Client sends data to the Server using JSON and is only sent upon startup of the Client.

To install ```wlp/bin/installUtility install LibertyRegistration.esa --to=usr```

Include in server.xml in the ```<featureManager>``` the following feature: ```<feature>usr:LibertyStatus</feature>```

The server.xml also requires the following information:
```<libertyRegistration host="hostname.domain.com" port="9443" protocol="https"/>```

Both HTTP and HTTPs protocols are supported.

The server application, ```LibertyRegistrationServer.war```, can be dropped into the dropins folder.
Include in following in the server.xml:

```    
    <featureManager>
        <feature>jaxrs-2.0</feature>
        <feature>jpa-2.1</feature>
        <feature>jsf-2.2</feature>
        <feature>ejbLite-3.2</feature>
    </featureManager>
 ```
 
 Also required is a dataSource with the ```jndiName="jdbc/LibertyRegistration" ```
 
 Example:
 ```
    <dataSource jndiName="jdbc/LibertyRegistration" statementCacheSize="10" type="javax.sql.ConnectionPoolDataSource">
        <jdbcDriver javax.sql.ConnectionPoolDataSource="com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource">
            <library>
                <fileset dir="${server.output.dir}/resources" includes="mysql-connector-java-5.1.43-bin.jar"/>
            </library>
        </jdbcDriver>
        <properties URL="jdbc:mysql://paskin.grishnackh.ibm.com:3306/devworks" password="{aes}ADJBkR+8qAkjs+xNKNbQYZlhma1F9pOpPcoRg1frgFQZ" user="brian"/>
    </dataSource>
```

