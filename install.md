# Application

NOTE - all execution steps mentioned in document are run using Java 11 ( Open JDK v11) and Apache Maven 3.6.3, with JAVA_HOME and java and maven in system PATH
- all commands were done in bash

## J2EE
- manually add a j2ee jar file into local maven repo
	- download it from - https://repo.maven.apache.org/maven2/milyn/j2ee/1.4/
	- install it - `mvn install:install-file -Dfile=j2ee-1.4.jar -DgroupId=javax.j2ee -DartifactId=j2ee -Dversion=1.4 -Dpackaging=jar`

## Common libs
- unpack `ark` repo
- unpack `util` repo
- position into root of each repo
- compile and install into local maven - `mvn clean install -DskipTests`

## Packaging
- unpack `notmotor-eclipse` repo
- position into root - `notmotor-eclipse\`
- install the superpom - `mvn install:install-file -Dpackaging=pom -Dfile=superpom-2.4.xml -DpomFile=superpom-2.4.xml`
- position into the `notmotor-eclipse\NotmotorBuild\` project
- package the apps - `mvn clean package -DskipTests`
- the final EAR files will be generated:
	- Admin - `notmotor-eclipse\NotmotorAdminEAR\target\notmotoradminear-2.0-SNAPSHOT.ear`
	- IPL - `notmotor-eclipse\NotmotorIplEAR\target\notmotoriplear-2.0-SNAPSHOT.ear`

# Infrastructure

## Setup
- unpack `notmotor-poc` repo
- position into project root - `notmotor-poc\`
- start containers from project root - `docker-compose up`
	- liberty will wait for db2 to be ready [takes 2-3min]
	- NOTE - on first start, DB2 will say it is ready BEFORE it really is; need to wait longer [wait for all the scripts to execute and the initial tables to be created]
- add missing DB2 tables [see below for db connection info]
	- execute all commands contained in `notmotor-poc\script.sql`

## Deployment
- place the compiled EAR files into `notmotor-poc\csn-liberty\app\dropin`
- edit the `server.xml` [located in `notmotor-poc\csn-liberty\config\dropin`]
	- at the bottom, the two `application` elements have an invalid filename [.ea insead of .ear]
	- fixing this will trigger the deployment of the app(s)
	- in case of changing the application, mimic undeployment/deployment by breaking and fixing this path
- the app is up and ready

# Testing

## IPL Application
- using WS requests:
	- with SoapUI, open the project in [poc repo] `notmotor-poc\support\soap\`
	- use operation:
		- skickaMeddlande - to send requests [for SMS or EMail delivery]
			- response will give `meddelandeId`
		- hamtaMeddlande - to fetch the request - use the above IDs
- app behavior:
	- SMS request - docker logs will display output from `csn_sms_mock`
		- NOTE - the mock server does NOT respond with a proper reposne, thus the message will have have a failed submission status [32]
	- EMail request - a new entry will appear in `notmotor-poc\support\mails\`
	- messages will also be stored within the `NOTMOTOR.MEDDELANDE` table
- using MQ requests [will send one SMS and one EMail request via JMS]:
	- position into `notmotor-eclipse\NotmotorMQTestClient\`
	- run it - `mvn compile exec:java -Dexec.mainClass="se.csn.notmotor.mqclient.MQClient"`

## Admin Application
- app will be available at `http://localhost:9080/notmotoradminwebb/`
	- auth: `adminAppUser`/`adminAppUser`
- use the `Sök meddelanden` or `Visa meddelande` to search and display messages
- `Ändra styrparametrar` - for changing the config params of the app [contents of `PARAMETER` table]
- `Schemalägg stängningar` - for scheduled downtime
- `Öppna/Stäng Notifieringsmotorn` - for start/stop of app instances, multiple instance start
- `Hämta statistik` - general statistic
- `Visa status` - app status

# Misc info

## DB conn. info
- type: DB2 LUW
- host: `localhost`
- port: `50000`
- database: `WDBINTE0`
- username: `db2inst1`
- password: `Lozinka123`
- schema: `NOTMOTOR`

## MQ conn. info
- if you have MQExplorer
- Queue manager name: `MQINT00`
- host: `localhost`
- port: `1415`
- channel: `DEV.ADMIN.SVRCONN`
- enable user id
	- userid: `admin`
	- password: `passw0rd`

## WAS setup
- within the `docker-compose.yaml`, the original WAS env is also available
- to setup WAS, uncomment the `csn_was` service
	- further steps are documented within `/csn-was/README.MD`
NOTE - you can have both WAS and Liberty running, but NOT the same apps on both. Undeploy/stop the application on one platform before starting on the other

## Dev env setup
- use eclipse - https://www.eclipse.org/downloads/packages/release/2020-06/r
