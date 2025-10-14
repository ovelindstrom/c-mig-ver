# Notmotor POC
- goal - to start it in liberty
	- before - to start it in was traditional
- major changes
	- update java - from 6 to 11
	- update dependencies

- server xml - configure from scripts
	- run config script on WAS 
		- cell/node changed
		- LDAP config. test disabled
		- CSN_WORKSPACE_ROOT, LOG_ROOT, DB2UNIVERSAL_JDBC_DRIVER_PATH changed
		- jdbcprovidercfg.classpath changed, fixed env var
		- disabled SECURITY_ADMINISTRATIVE
		- changed security_j2cauthcfg - set db2 user/pass
		- changed jms_mq_qcfactorycfg - port, channel, user/pass
	- extract config. elements
		- datasource, mq, ldap...
	- enriched war files
		- added .properties file for admin

## Support
- DB2 with tables
	- create DB with proper host, name, schema and tables
		- test connection from was admin
- MQ with queues
	- create MQ with proper host, manager, queues, channels and access rights
	
########

# Steps taken

## Infrastructure setup
- docker compose used to setup dev env
- db2, mq; mock smtp and http echo servers
- WAS - `ibmcom/websphere-traditional:8.5.5.22-ubi`
- Liberty - `open-liberty:22.0.0.10-full-java11-openj9`
	- also used - `ibmcom/websphere-liberty:21.0.0.6-kernel-java11-openj9-ubi`
- first focus was to recreate as-is state on WAS
- then to get it working on liberty

## WAS config
- based on provided `was_notmotor_setup.jy`
  - win->unix path changes
  - changes: cell/node names, db2 driver loc, auth info, mq config
  - a fix for `jdbcprovidercfg.classpath`
  - ldap config skipped
  - security_administrative.create_update skipped
- image setup
  - property files set within image
  - same path added to server classpath [manually after server start]
- manual app deployment after server startup - used original EAR-s provided by CSN
	
## Liberty config
- feature setup taken from IBM migration assistant
- mirror setup from WAS config as much as possible
  - mq and db2 drivers setup
  - data-source, jms q. connection fact., conn. manager, queue and jms activation spec. configured with same JNDI names
  - app property files placed in appropriate locations
  	- app classloader set to access those locations
- basic registry for security
- all liberty config is done within server.xml, no extra config after server startup
	
## Code changes - for liberty
- converted the code into proper maven structure
  - removed all jars from build path and various lib directories
  - proper maven dependency setup
  - removed all `provided` scopes
  	- some exceptions - myfaces impl
- extracted the code from ark and utils shared libs, created separate projects to be able to update them
- switched from java 1.6 to java 11
- removed WAS configs from project facet and WAS as the target runtime
- removed all WAS specific dependencies [e.g `com.ibm.websphere.was packages`]
  - some problematic [but unused] classes completely commented out [e.g. `SparaTillEDHFraga`, `WASAdminClient`]
- minimal code change made, focus on dependencies and some configs (`faces-config.xml`)
  - removed `ServletListner` usage from servlets 
  - removed `RASLogger`
  - some test classes were even fixed :)
- all modified artifacts [e.g. notmotorjar, ark,  notmotoriplclient, notmotoriplwebb, ... ] given new versions [next major] and packaged inside new EARs	
	
## Tests
- first on WAS as a benchmark
  - WS requests for SMS and email notifications; create & get operations
  - MQ requests for SMS and email notifications
  	- leveraged the provided NotmotorMQTestClient project to send appropriate messages
  - Admin screen interactions
  	- fetch stats
  	- message search/fetch
  	- config updates
  	- start/pause/stop commands
  	- multiple instance scenarios
- same checks done on liberty
  - myfaces issues for admin interactions; fixed after removing all [was] ibm-jsf references, and switch to apache myfaces [liberty provided]
