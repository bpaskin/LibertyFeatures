CREATE TABLE ServerInfo (
   hostname VARCHAR(25) NOT NULL,
   serverName VARCHAR(25) NOT NULL,
   libertyVersion VARCHAR(11) NOT NULL,
   jdkVersion VARCHAR(3) NOT NULL,
   jreVersion VARCHAR(50) NOT NULL,
   CONSTRAINT PK_Server PRIMARY KEY (hostname,serverName)
);
