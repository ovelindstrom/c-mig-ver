# NotmotorIplClient Module - Architecture Analysis

## Overview

**NotmotorIplClient** is a standalone JAR library that provides a **SOAP web service client** for external applications to communicate with the NotmotorIPL system. It acts as a **remote interface** for other CSN applications to send messages through the notification engine.

## Module Structure

```
NotmotorIplClient/
├── pom.xml (Java 11, AXIS 1.3, JAX-RPC 1.1.4)
└── src/main/java/se/csn/webservice/bas/notmotor/skicka/
    ├── SkickaProxy.java              (Main client entry point)
    ├── Skicka_ServiceLocator.java    (AXIS service locator)
    ├── Skicka_PortType.java          (SOAP interface)
    ├── SkickaSOAPStub.java           (AXIS stub implementation)
    ├── Skicka_Service.java           (Service interface)
    └── DTO classes/                  (Data Transfer Objects)
        ├── DTOMeddelande.java        (Message)
        ├── DTONotifieringResultat.java (Result)
        ├── DTOAvsandare.java         (Sender)
        ├── DTOMottagare.java         (Recipient)
        ├── DTOBilaga.java            (Attachment)
        └── DTOMeddelandeHandelse.java (Message event)
```

## Communication Architecture

### 1. Client-Side (NotmotorIplClient)

```
External Application
    ↓
SkickaProxy (client-side proxy)
    ↓
Skicka_ServiceLocator (AXIS service locator)
    ↓
SkickaSOAPStub (AXIS stub - marshals Java ↔ SOAP XML)
    ↓
HTTP/SOAP Request
    ↓
[Network: http://webservice.csn.se/bas/notmotor/skicka]
```

### 2. Server-Side (NotmotorIplWebb)

```
[Network: /services/SkickaSOAP]
    ↓
AxisServlet (org.apache.axis.transport.http.AxisServlet)
    ↓
SkickaSOAPSkeleton (server-side skeleton - unmarshals SOAP XML ↔ Java)
    ↓
SkickaSOAPImpl (implementation)
    ↓
ConvertDTO (DTO ↔ internal model conversion)
    ↓
SkickaService (business logic)
    ↓
NotifieringProxy (interface to EJB layer)
    ↓
[Database/JMS operations in NotmotorIplEjb]
```

## Key Components

### SkickaProxy (Client Entry Point)

**Purpose:** Simple facade for external applications to call NotmotorIPL services.

**Key Features:**
- Configurable endpoint URL (default: `http://webservice.csn.se/bas/notmotor/skicka`)
- Three main operations:
  1. `skickaMeddelande(DTOMeddelande)` - Send a message
  2. `hamtaMeddelande(long)` - Retrieve a message by ID
  3. `taBortMeddelande(long)` - Delete a message

**Usage Example:**
```java
SkickaProxy proxy = new SkickaProxy();
proxy.setEndpoint("https://localhost:9443/NotmotorIPL/services/SkickaSOAP");

DTOMeddelande meddelande = new DTOMeddelande();
meddelande.setRubrik("Test Subject");
meddelande.setMeddelandetext("Test message body");
// ... set sender, recipients, etc.

DTONotifieringResultat resultat = proxy.skickaMeddelande(meddelande);
```

### Skicka_ServiceLocator (AXIS Service Locator)

**Purpose:** AXIS-generated class that locates and creates SOAP service stubs.

**Key Configuration:**
- Default endpoint: `http://webservice.csn.se/bas/notmotor/skicka`
- WSDD service name: `SkickaSOAP`
- Uses `SkickaSOAPStub` for actual SOAP communication

**How it works:**
1. Creates `SkickaSOAPStub` instance with endpoint URL
2. Configures type mappings (Java ↔ XML serialization)
3. Returns stub implementing `Skicka_PortType` interface

### SkickaSOAPStub (AXIS Stub)

**Purpose:** Auto-generated AXIS stub that handles SOAP message marshalling/unmarshalling.

**Key Responsibilities:**
- Serializes Java objects (DTOMeddelande, etc.) to SOAP XML
- Deserializes SOAP XML responses to Java objects
- Configures AXIS type mappings for all DTO classes
- Handles SOAP call invocation via `org.apache.axis.client.Call`

**Type Mappings:**
All DTO classes have AXIS serializers/deserializers:
- `BeanSerializerFactory` / `BeanDeserializerFactory` for complex objects
- `ArraySerializerFactory` / `ArrayDeserializerFactory` for arrays
- Maps to namespace: `http://webservice.csn.se/bas/notmotor/skicka`

### Data Transfer Objects (DTOs)

**DTOMeddelande** (Message):
- `id` - Message ID
- `csnnummer` - Student/citizen number
- `rubrik` - Subject
- `meddelandetext` - Message body
- `rubrikEncoding` / `meddelandeEncoding` - Character encodings
- `bilagor[]` - Attachments array
- `skapad` / `skickat` / `skickaTidigast` - Timestamps
- `avsandare` - Sender
- `mottagare[]` - Recipients array
- `handelser[]` - Message events array
- `callbackURL` / `callbackMask` - Callback configuration
- `mimetyp` - MIME type
- `kanal` - Channel (e.g., email, SMS)

**DTONotifieringResultat** (Result):
- Message ID (if successful)
- Status code
- Error message (if failed)

## Server-Side Implementation (NotmotorIplWebb)

### AXIS Configuration

**web.xml:**
```xml
<servlet>
  <servlet-name>AxisServlet</servlet-name>
  <servlet-class>org.apache.axis.transport.http.AxisServlet</servlet-class>
</servlet>

<servlet-mapping>
  <servlet-name>AxisServlet</servlet-name>
  <url-pattern>/services/*</url-pattern>
</servlet-mapping>
```

**Actual URL Pattern:**
- Client calls: `http://host:port/NotmotorIPL/services/SkickaSOAP`
- AxisServlet routes to service defined in `server-config.wsdd`

### WSDD Configuration (server-config.wsdd)

**Location:** `NotmotorIplWebb/src/main/webapp/WEB-INF/server-config.wsdd`

**Key Configuration:**
```xml
<service name="SkickaSOAP" provider="java:RPC" style="document" use="literal">
  <parameter name="allowedMethods" value="*"/>
  <parameter name="typeMappingVersion" value="1.2"/>
  <parameter name="wsdlPortType" value="Skicka"/>
  <parameter name="className" value="se.csn.webservice.bas.notmotor.skicka.SkickaSOAPSkeleton"/>
  <parameter name="wsdlServicePort" value="SkickaSOAP"/>
  <parameter name="schemaQualified" value="http://webservice.csn.se/bas/notmotor/skicka"/>
  
  <!-- Type mappings for all DTO classes -->
  <typeMapping ... />
</service>
```

### SkickaSOAPImpl (Server Implementation)

**Location:** `NotmotorIplWebb/src/main/java/se/csn/webservice/bas/notmotor/skicka/SkickaSOAPImpl.java`

**Implementation:**
```java
public class SkickaSOAPImpl implements Skicka_PortType {
    @Override
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) {
        SkickaService skicka = new SkickaService();
        // Convert web service DTO → internal model
        return ConvertDTO.getNotifieringresultat(
            skicka.skickaMeddelande(ConvertDTO.getMeddelande(parameters)));
    }
    // ... other methods
}
```

**Key Points:**
- Implements `Skicka_PortType` interface (shared with client)
- Uses `ConvertDTO` utility to convert between:
  - Web service DTOs (se.csn.webservice.bas.notmotor.skicka.DTO*)
  - Internal models (se.csn.notmotor.ipl.model.*)
- Delegates business logic to `SkickaService`

### SkickaService (Business Logic Layer)

**Location:** `NotmotorIplWebb/src/main/java/se/csn/notmotor/ipl/webservice/SkickaService.java`

**Responsibilities:**
- Implements `NotifieringProxy` interface
- Delegates to actual proxy implementation (set via factory pattern)
- Provides additional search operations:
  - `sokAvsandare()` - Search senders
  - `sokMottagare()` - Search recipients
  - `sokMeddelanden()` - Search messages

**Architecture Pattern:**
```java
public class SkickaService implements NotifieringProxy {
    private static NotifieringProxyFactory factory;
    
    private NotifieringProxy getProxy() {
        return factory.getNotifieringProxy();
    }
    
    public NotifieringResultat skickaMeddelande(Meddelande meddelande) {
        return getProxy().skickaMeddelande(meddelande);
    }
}
```

**Key Design:**
- Uses **Factory Pattern** - actual implementation injected via `setFactory()`
- Initialized in `InitServlet.init()` during application startup
- Allows different implementations (EJB, direct database, mock, etc.)

## Communication Protocol

### SOAP Message Example

**Request (skickaMeddelande):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:skic="http://webservice.csn.se/bas/notmotor/skicka">
  <soapenv:Body>
    <skic:skickaMeddelande>
      <parameters>
        <csnnummer>123456789</csnnummer>
        <rubrik>Important Notice</rubrik>
        <meddelandetext>Your loan has been approved.</meddelandetext>
        <avsandare>
          <namn>CSN Customer Service</namn>
          <adress>kundtjanst@csn.se</adress>
        </avsandare>
        <mottagare>
          <namn>John Doe</namn>
          <adress>john.doe@example.com</adress>
          <typ>EMAIL</typ>
        </mottagare>
        <kanal>EMAIL</kanal>
      </parameters>
    </skic:skickaMeddelande>
  </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
  <soapenv:Body>
    <skickaMeddelandeResponse xmlns="http://webservice.csn.se/bas/notmotor/skicka">
      <result>
        <id>100234</id>
        <status>OK</status>
        <meddelande>Message queued successfully</meddelande>
      </result>
    </skickaMeddelandeResponse>
  </soapenv:Body>
</soapenv:Envelope>
```

## Technology Stack

### Client Dependencies (pom.xml)

```xml
<dependencies>
  <!-- Apache AXIS 1.3 (legacy SOAP framework) -->
  <dependency>
    <groupId>axis</groupId>
    <artifactId>axis</artifactId>
    <version>1.3</version>
  </dependency>
  
  <!-- JAX-RPC API (Java API for XML-based RPC) -->
  <dependency>
    <groupId>jakarta.xml.rpc</groupId>
    <artifactId>jakarta.xml.rpc-api</artifactId>
    <version>1.1.4</version>
  </dependency>
</dependencies>
```

**Key Technologies:**
- **AXIS 1.3** - Legacy Apache SOAP engine (circa 2004)
- **JAX-RPC 1.1** - Java XML RPC API (pre-JAX-WS)
- **Document/Literal style** - Modern SOAP style (not RPC/encoded)

## Migration Considerations

### Current State (AXIS 1.3)
✅ **Pros:**
- Working implementation
- Document/literal style (WS-I compliant)
- Shared DTO classes between client/server

❌ **Cons:**
- AXIS 1.3 is **ancient** (2004)
- Known security vulnerabilities (CVEs)
- Not actively maintained
- Pre-Java EE 5 technology

### Modern Alternatives

#### Option 1: JAX-WS (Java EE 7 Native)
**Liberty Feature:** `jaxws-2.2`

**Benefits:**
- Native Java EE 7 support
- No external dependencies
- Annotation-based (`@WebService`, `@WebMethod`)
- Better tooling (wsimport)

**Migration Path:**
1. Generate client from WSDL using `wsimport`
2. Replace AXIS stubs with JAX-WS proxies
3. Remove AXIS dependencies
4. Update server-side to use `@WebService` annotations

**Example Server:**
```java
@WebService(serviceName = "Skicka", portName = "SkickaSOAP",
            targetNamespace = "http://webservice.csn.se/bas/notmotor/skicka")
public class SkickaWebService {
    @WebMethod
    public DTONotifieringResultat skickaMeddelande(DTOMeddelande parameters) {
        // Implementation
    }
}
```

**Example Client:**
```java
// Auto-generated by wsimport
SkickaService service = new SkickaService();
Skicka port = service.getSkickaSOAP();
DTONotifieringResultat result = port.skickaMeddelande(meddelande);
```

#### Option 2: REST/JSON API
**Liberty Feature:** `jaxrs-2.1`

**Benefits:**
- Modern, lightweight
- JSON instead of XML
- Better performance
- Easier debugging/testing

**Trade-offs:**
- Not SOAP (may break existing clients)
- Would require parallel deployment during transition

#### Option 3: Keep AXIS (Short-term)
**Acceptable if:**
- No critical CVEs in AXIS 1.3
- Migration timeline is short
- Focus on other priorities first (Quartz, properties, etc.)

## Security Considerations

### Current Issues
1. **No authentication** - SOAP endpoint is open
2. **No encryption** - HTTP (not HTTPS) default endpoint
3. **AXIS 1.3 CVEs** - Known vulnerabilities in old version

### Recommendations
1. **Immediate:**
   - Force HTTPS in production (`server.xml` redirect)
   - Add servlet filter for authentication
   - Update AXIS to latest 1.x if available

2. **Short-term:**
   - Migrate to JAX-WS 2.2
   - Implement WS-Security headers
   - Add request validation

3. **Long-term:**
   - Consider REST API with OAuth2
   - API Gateway for rate limiting, monitoring

## Usage in CSN Ecosystem

**Who uses NotmotorIplClient?**
- Other CSN applications needing to send notifications
- Batch jobs scheduling message delivery
- External systems (possibly other government agencies)

**Integration Pattern:**
```java
// External application (e.g., LåneSystemet)
import se.csn.webservice.bas.notmotor.skicka.SkickaProxy;

public class LoanApprovalService {
    private SkickaProxy notmotorClient;
    
    public void sendApprovalNotification(Loan loan) {
        DTOMeddelande msg = new DTOMeddelande();
        msg.setRubrik("Lån beviljat");
        msg.setMeddelandetext("Ditt lån har godkänts...");
        
        notmotorClient.skickaMeddelande(msg);
    }
}
```

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│ External Application (e.g., Batch Job Server)              │
│   └── NotmotorIplClient.jar (AXIS client library)         │
└──────────────────────┬──────────────────────────────────────┘
                       │ SOAP/HTTP
                       ▼
┌─────────────────────────────────────────────────────────────┐
│ OpenLiberty Server (NotmotorIPL Application)               │
│   ├── AxisServlet (/services/*)                            │
│   ├── SkickaSOAPImpl (SOAP implementation)                 │
│   ├── SkickaService (business logic)                       │
│   └── NotifieringProxy → EJB Layer                         │
│       └── NotmotorIplEjb.jar                               │
│           ├── Database operations (DB2)                    │
│           └── JMS operations (RabbitMQ)                    │
└─────────────────────────────────────────────────────────────┘
```

## Conclusion

### Summary
- **NotmotorIplClient** provides a **SOAP-based remote interface** to NotmotorIPL
- Uses **Apache AXIS 1.3** (legacy but functional)
- **Three main operations:** send, retrieve, delete messages
- **DTO-based communication** with full type safety
- **Server-side** uses AXIS servlet + WSDD deployment descriptors
- **Business logic** separated via factory pattern (good design)

### Recommendation Priority
1. **High Priority:** Replace Quartz scheduler (blocking properties issue)
2. **Medium Priority:** Migrate AXIS → JAX-WS (security, maintainability)
3. **Low Priority:** Consider REST API (long-term modernization)

### Next Steps if Migrating to JAX-WS
1. Extract WSDL from current AXIS deployment
2. Generate JAX-WS client using `wsimport`
3. Update server to use `@WebService` annotations
4. Test parallel deployment (AXIS + JAX-WS)
5. Migrate client applications one by one
6. Deprecate AXIS endpoints
