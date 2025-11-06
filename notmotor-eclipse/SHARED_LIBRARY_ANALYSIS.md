# Shared Library Analysis - Notmotor EARs Dependencies

## Overview
This document identifies common dependencies across NotmotorIplEAR and NotmotorAdminEAR that should be packaged into a shared Liberty library to reduce classpath conflicts and duplicate loading.

## Common Dependencies (Used by Both EARs)

These dependencies appear in multiple EARs and should be packaged as a shared library:

### Notmotor Internal Libraries (Core)
```
se.csn:ark:3.0.1
se.csn:utils:1.0.0
se.csn.notmotor:notmotorjar:2.1-SNAPSHOT
se.csn:notmotoriplclient:2.1-SNAPSHOT
```
**Reason**: Used by both IPL and Admin applications, provides core domain models and utilities
**Scope**: `compile`

### Logging
```
log4j:log4j:1.2.17
```
**Reason**: Used by both IplWebb, AdminWebb, and EJBs for application logging
**Scope**: `compile`

### SOAP/XML Communication
```
axis:axis:1.3 (with exclusion of axis-jaxrpc)
jakarta.xml.rpc:jakarta.xml.rpc-api:1.1.4
```
**Reason**: Required for SOAP web service communication in both EARs
**Scope**: `compile`

### HTTP Client (for external calls)
```
commons-httpclient:commons-httpclient:3.1
```
**Reason**: Used for HTTP client calls, needed by utility functions
**Scope**: `compile`

### Collections Utilities
```
commons-collections:commons-collections:3.2.2
```
**Reason**: Referenced in both Jar module and Web modules
**Scope**: `compile`

### Scheduler
```
opensymphony:quartz:1.6.0
```
**Reason**: Used for scheduled tasks in both applications
**Scope**: `compile`

### XML Processing
```
xerces:xercesImpl:2.8.1 (with exclusion of xml-apis)
```
**Reason**: XML parsing for web services and configuration
**Scope**: `compile`

### String Utilities
```
org.apache.commons:commons-lang3:3.19.0
```
**Reason**: Used by both Jar module and utility functions
**Scope**: `compile`

### Mail/SMTP (Optional but common)
```
com.sun.mail:smtp:1.6.2
```
**Reason**: For email notifications, used in NotmotorJar
**Scope**: `compile`

## IplEAR-Only Dependencies

These dependencies are specific to NotmotorIplEAR and should **NOT** go in shared library:
- None currently (both WARs and EJB inherit from parent)

## AdminEAR-Only Dependencies

These dependencies are specific to NotmotorAdminEAR and should **NOT** go in shared library:
- None currently (already includes all core libraries)

## NOT Including (Already provided by Liberty Features)

**Do NOT include in shared library** - provided by Liberty features:
```
javax:javaee-api:7.0 (feature: javaee-7.0)
org.eclipse.microprofile:microprofile:1.4 (feature: microProfile-1.4)
com.sun.faces:jsf-api:2.2.20 (feature: jsf-2.2)
com.sun.faces:jsf-impl:2.2.20 (feature: jsf-2.2)
javax.el:javax.el-api:3.0.0 (feature: javaee-7.0)
javax.servlet:jstl:1.2 (feature: javaee-7.0)
javax.activation:activation:1.1.1 (feature: javaee-7.0)
io.opentelemetry:opentelemetry-api:1.32.0 (feature: mpTelemetry-2.0)
io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:2.7.0 (feature: mpTelemetry-2.0)
```

## Implementation Strategy

### Step 1: Create a Shared Library Module (Optional but Recommended)

Create `NotmotorSharedLib/pom.xml` as a new assembly module:
```xml
<groupId>se.csn.notmotor</groupId>
<artifactId>notmotor-shared-lib</artifactId>
<version>2.1-SNAPSHOT</version>
<packaging>jar</packaging>
```

Include the common dependencies in `<dependencyManagement>` via parent (NotmotorBuild).

### Step 2: Update NotmotorLiberty server.xml

Add a shared library configuration in `server.xml`:
```xml
<library id="notmotorSharedLib">
    <fileset dir="${server.config.dir}/lib/shared" includes="*.jar"/>
</library>
````

### Step 3: Copy Dependencies to Shared Library

Add to NotmotorLiberty `pom.xml` a new execution to copy the common dependencies:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-shared-lib</id>
            <phase>pre-package</phase>
            <goals>
                <goal>copy</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>se.csn</groupId>
                        <artifactId>ark</artifactId>
                        <version>3.0.1</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>se.csn</groupId>
                        <artifactId>utils</artifactId>
                        <version>1.0.0</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>se.csn.notmotor</groupId>
                        <artifactId>notmotorjar</artifactId>
                        <version>2.1-SNAPSHOT</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>se.csn</groupId>
                        <artifactId>notmotoriplclient</artifactId>
                        <version>2.1-SNAPSHOT</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>log4j</groupId>
                        <artifactId>log4j</artifactId>
                        <version>1.2.17</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>axis</groupId>
                        <artifactId>axis</artifactId>
                        <version>1.3</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>jakarta.xml.rpc</groupId>
                        <artifactId>jakarta.xml.rpc-api</artifactId>
                        <version>1.1.4</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>commons-httpclient</groupId>
                        <artifactId>commons-httpclient</artifactId>
                        <version>3.1</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>commons-collections</groupId>
                        <artifactId>commons-collections</artifactId>
                        <version>3.2.2</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>opensymphony</groupId>
                        <artifactId>quartz</artifactId>
                        <version>1.6.0</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>xerces</groupId>
                        <artifactId>xercesImpl</artifactId>
                        <version>2.8.1</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>org.apache.commons</groupId>
                        <artifactId>commons-lang3</artifactId>
                        <version>3.19.0</version>
                    </artifactItem>
                    <artifactItem>
                        <groupId>com.sun.mail</groupId>
                        <artifactId>smtp</artifactId>
                        <version>1.6.2</version>
                    </artifactItem>
                </artifactItems>
                <outputDirectory>${project.build.directory}/liberty/wlp/usr/shared/resources/lib</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Step 4: Update EAR POMs (Optional)

Remove transitive dependencies from EARs by marking as `<scope>provided</scope>` since they'll now be in the shared library:

```xml
<dependency>
    <groupId>se.csn</groupId>
    <artifactId>ark</artifactId>
    <scope>provided</scope>
</dependency>
```

## Benefits

1. **Reduced EAR Size**: Dependencies not packaged in each EAR
2. **Single Copy at Runtime**: All apps share same library instances
3. **Easier Updates**: Update shared library once, all apps benefit
4. **Cleaner Classpath**: Reduces classpath pollution and conflicts
5. **Faster Deployment**: Smaller EARs deploy faster

## Risks & Mitigations

| Risk | Mitigation |
|------|-----------|
| Version conflicts if different apps need different versions | Use same versions for all apps (currently already do this) |
| Shared library updates affect all apps | Test updates thoroughly; use immutable deployments |
| Harder to debug classloader issues | Document library structure in README |

## Migration Path

1. **Phase 1**: Test with current approach (keep in EARs)
2. **Phase 2**: Move only Notmotor internal libs to shared library
3. **Phase 3**: Move SOAP/logging dependencies once stable
4. **Phase 4**: Complete shared library if no issues arise

---

**Total Dependencies for Shared Library**: ~13 JARs
**Estimated Size Savings**: 10-15MB per EAR (depending on transitive deps)
