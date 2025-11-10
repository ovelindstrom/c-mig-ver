# AI Coding Agent Instructions - Notmotor Liberty Migration

This codebase is a **Java 11 migration of legacy Notmotor application from WebSphere Traditional (WASM) to OpenLiberty**, with a focus on containerization and OpenTelemetry observability.

## Architecture Overview

### Module Structure
The project follows a parent-child Maven hierarchy rooted at `NotmotorBuild/pom.xml` (parent: `se.csn:superpom:3.0-SNAPSHOT`). Each module has a specific role:

- **NotmotorJar**: Shared models, entities, and utilities across modules (version 2.1-SNAPSHOT)
- **NotmotorIplWebb/NotmotorAdminWebb**: WAR modules containing JSF/SOAP services and admin UI
- **NotmotorIplEjb**: Enterprise Java Beans for integration platform (EJB 3.2)
- **NotmotorIplEAR/NotmotorAdminEAR**: EAR packaging combining WAR, EJB, and Jar modules (EAR 10)
- **NotmotorIplClient**: Standalone client library for external system integration
- **NotmotorLiberty**: OpenLiberty server runtime with Dockerfile for containerization
- **NotmotorProperties/GemensammaProperties**: Configuration and log properties (separated by area. IW = Internal Webb, that is websites that are used by CSN staff. IPL = Integration Platform, that is services used for communication with other internal and external systems.)

### Key Dependencies & Versions
```xml
<!-- Core CSN Libraries (with J2EE exclusions) -->
se.csn:ark:3.0.1
se.csn:utils:1.0.0

<!-- Java EE / Jakarta EE -->
javax:javaee-api:7.0 (provided)
jakarta.xml.rpc:jakarta.xml.rpc-api:1.1.4

<!-- Logging & Legacy -->
log4j:log4j:1.2.17  ← Migrate to Log4j2 eventually
org.apache.commons:commons-lang3:3.19.0
commons-collections:commons-collections:3.2.2

<!-- JSF -->
com.sun.faces:jsf-api:2.2.20
com.sun.faces:jsf-impl:2.2.20

<!-- Testing -->
junit:junit:4.13 (test scope)
easymock:easymock:1.2_Java1.3 (test scope)
```

**Critical Migration Notes:**
- All `javax.j2ee:j2ee` dependencies must be excluded (legacy, conflicts with modern Java)
- AXIS 1.3 is legacy SOAP; keep as-is for backward compatibility
- JSF 2.2 is supported by Liberty; avoid ibm-jsf references

## Development Workflow

### Prerequisites
1. JDK 11 (minimum) - Liberty requires Java 11+
2. Maven 3.8.9+ (3.9.11 preferred for security)
3. Optional: Open J9 JDK (IBM Semeru) for parity with production containers

### Build & Run Locally

**Standard build** (all modules):
```bash
cd /Users/ovelindstrom/CSN/PoC/csn-migrate/notmotor-eclipse/NotmotorBuild
mvn clean package
```

**Liberty development mode** (hot reload):
```bash
cd NotmotorLiberty
./mvnw liberty:dev  # Linux/macOS
# OR
./mvnw liberty:devc  # Containerized dev mode (requires Docker/Podman)
```

**Full docker-compose stack** (Liberty + OTEL observability + DevMail):
```bash
cd NotmotorLiberty
podman compose -f docker-compose.dev.yml up -d --build
# Then: make devc (if Makefile available)
```

**Key build profiles** (in NotmotorBuild/pom.xml):
- `poms`: OpenRewrite recipe runner for POM modernization (`mvn -Ppoms ...`)
- `wamt`: IBM WAMT-recommended Java 11 migration recipes
- `rewrite`: Full modernization recipes

### Testing
```bash
mvn -f NotmotorBuild/pom.xml test  # JUnit 3/4 tests across modules
mvn -f NotmotorBuild/pom.xml verify  # Integration tests via failsafe
```

## Code Migration Patterns

### Java 11 Compliance
- **Target release**: `11` (configured in all compiler plugins)
- **Charset**: Always UTF-8 (specified in parent pom via `<project.build.sourceEncoding>`)
- **Pre-package JavaDoc**: Must be removed or moved to class-level (not allowed before `package` statement)
  - Use Python script: `remove_pre_package.py` for bulk fixes
- **Missing `@Override`**: Use OpenRewrite recipe `org.openrewrite.staticanalysis.MissingOverrideAnnotation`

### Dependency Modernization (OpenRewrite)
**Example:** Migrate Commons Lang 2 → 3 (removes internal API usage):
```bash
mvn -U org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-apache:RELEASE \
  -Drewrite.activeRecipes=org.openrewrite.apache.commons.lang.UpgradeApacheCommonsLang_2_3
```

**Key recipes** (defined in `NotmotorBuild/rewrite-pom.yml`):
- `se.csn.recipes.modernize.NewSuperPomParent` - Update to superpom 3.0+
- `se.csn.recipes.modernize.DropDevelopers` - Remove `<developers>` XML blocks
- `se.csn.recipes.modernize.DropSourceDirectory` - Use Maven standard layout

### WAR/EAR Module Patterns

**WAR modules** (NotmotorIplWebb, NotmotorAdminWebb):
- Must follow Maven standard layout: `src/main/{java,resources,webapp}`
- JSF backing beans: Use `@RequestScoped` + `@Named` (CDI 1.0+)
- SOAP services: Declare with `@WebService` annotation (javax.jws package)
- Properties: Load from classpath via `WEB-INF/classes/ark_log4j.properties`

**EAR modules** (configuration in parent pom.xml):
```xml
<plugin>
  <artifactId>maven-ear-plugin</artifactId>
  <configuration>
    <version>10</version>
    <defaultLibBundleDir>lib</defaultLibBundleDir>
    <generateApplicationXml>false</generateApplicationXml>
  </configuration>
</plugin>
```
- Use `ejb.jar` + `web.war` + `app.jar` structure
- No `application.xml` needed (Liberty auto-detects)

## OpenLiberty Runtime Configuration

### Server XML Location
`NotmotorLiberty/src/main/webapp/WEB-INF/server.xml`

**Features required** (from WAMT analysis):
```xml
<featureManager>
  <feature>javaee-7.0</feature>
  <feature>webProfile-7.0</feature>
  <feature>ejb-3.2</feature>
  <feature>jaxws-2.2</feature>
  <feature>jaxb-2.2</feature>
  <!-- Observability -->
  <feature>mpOpenTracing-1.1</feature>
  <feature>mpMetrics-1.1</feature>
</featureManager>
```

### Property Files & JNDI
- **Data sources**: JNDI name `jdbc/CSNDataSource` (configured in server.xml)
- **MQ**: JNDI name `jms/QueueConnectionFactory` (Liberty provides IBM MQ driver)
- **Log4j properties**: Place in `${server.output.dir}/conf/` and reference via `log4j.configuration` system property

### Database & Messaging
- **DB2**: Liberty bundles `com.ibm.db2.jcc` (db2jcc4.jar via Maven dependency)
- **IBM MQ**: Liberty provides JMS adapter; configure via `<jmsConnectionFactory>` in server.xml
- **LDAP**: Support via `<federatedRepository>` (optional, currently disabled in config)

## Observability (OpenTelemetry)

### OTEL Integration
- **Exporter**: OTLP HTTP to `http://otel:4317` (docker-compose variable)
- **Trace annotations**: Use `@WithSpan` + `@SpanAttribute` from `io.opentelemetry.instrumentation.annotations`
- **Service name**: Configured via `OTEL_SERVICE_NAME` environment variable

**Example service with tracing** (`NotmotorLiberty/src/main/java/se/csn/applift/services/StringService.java`):
```java
@ApplicationScoped
public class StringService {
  @WithSpan
  public String processText(@SpanAttribute String input) { ... }
}
```

### Logging Strategy
- **Log4j 1.2.17** currently used; eventually migrate to Log4j2
- **Property substitution**: Use `${server.output.dir}` (Liberty variable) instead of `${SERVER_LOG_ROOT}` (WAS)
- **Log location**: `/logs/notmotor/` (configured in properties files)

## Encoding & Format Cleanup

### Critical Scripts
- **`fix_encoding.py`**: Bulk UTF-8/ISO-8859-1 remediation across all files
- **`fix_maven_encoding_errors.sh`**: Catches remaining Maven build encoding errors
- **`remove_pre_package.py`**: Strip pre-package JavaDoc (aggressive)
- **`maven_structure_converter.py`**: Reorganize source folders to Maven standard layout

### Format Standards
- **Source encoding**: UTF-8 only
- **Trailing whitespace**: Remove via `org.openrewrite.java.format.RemoveTrailingWhitespace`
- **Auto-format**: `org.openrewrite.java.format.AutoFormat` with Google Java Format style

## Integration Points & External Dependencies

### CSN Shared Libraries
- `se.csn:ark:3.0.1` - Common servlet/utility base classes (must exclude j2ee)
- `se.csn:utils:1.0.0` - Utility functions (exclude xalan and j2ee)
- `se.csn:notmotoriplclient:2.1-SNAPSHOT` - Client for inter-service calls

### Third-Party WASM → Liberty Removals
**Do NOT include** in Liberty builds:
- `com.ibm.websphere.*` packages (WAS-specific)
- `com.ibm.ejs.caching.ObjectCacheConfig` (EJB cache API)
- `org.apache.soap.*` (use javax.xml.rpc instead)
- Any `ibm-jsf` dependencies (use Liberty-provided Apache MyFaces)

### SOAP/WSDL Handling
- WSDL files: Keep in `src/main/webapp/WEB-INF/wsdl/`
- SOAP endpoints: Use `@WebService(serviceName=...)` + `@SOAPBinding`
- Client generation: Via `wsimport` Maven plugin (if needed)

## Containerization

### Docker/Podman Build
```dockerfile
# Use Liberty base image
FROM open-liberty:22.0.0.10-full-java11-openj9

# Copy server.xml
COPY src/main/webapp/WEB-INF/server.xml /config/

# Copy EARs/WARs
COPY target/notmotoriplear-2.0-SNAPSHOT.ear /config/apps/
COPY target/notmotoradminear-2.0-SNAPSHOT.ear /config/apps/
```

**Environment variables for Liberty**:
```bash
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317
OTEL_SERVICE_NAME=notmotor
SERVER_SHUTDOWN_TIMEOUT=60
```

### Compose Setup (Development)
Docker-compose files organize OTEL stack:
- **otel** service: Grafana LGTM (Loki, Tempo, Metrics, Pyroscope)
- **notmotor-liberty** service: App container (auto-rebuild in dev mode)
- Network: `csn-dev-net` (shared for inter-service communication)

## Common Gotchas & Troubleshooting

1. **Build fails with encoding errors**: Run `fix_encoding.py` before full build
2. **"LST tree not positive"** during OpenRewrite**: Pre-package JavaDoc or incomplete imports; check OPEN_REWRITE.md
3. **EAR deployment in Liberty**: Ensure `application.xml` is not generated (`generateApplicationXml=false`)
4. **SOAP services not found**: Verify `@WebService` annotation present and `server.xml` has `jaxws-2.2` feature
5. **Lost properties at runtime**: Confirm `WEB-INF/classes/` resource copying in WAR plugin config
6. **Container DNS issues**: Add `--network csn-dev-net` to Liberty container run opts; ensure `dns_enabled=true`

## References
- **MIGRATE.md**: Step-by-step modernization guide (Swedish)
- **OPEN_REWRITE.md**: OpenRewrite recipe deep-dive
- **NotmotorLiberty/README.md**: Liberty-specific build & deploy instructions
- **OpenRewrite recipes**: https://docs.openrewrite.org/recipes
- **Open Liberty guides**: https://openliberty.io/guides/
