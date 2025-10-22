# CSN Application Shift till OpenLiberty och OpenShift

Det här är ett "tomt" template-projekt som bara innehåller det minimalistiska som behöver finnas för att starta "Hello World" i en OpenLiberty WS.

Det innehåller också ett lite exempel på hur man kan konfigurera loggar, trace och metrik till OTEL-stacken.

## Bygg och starta lokalt

För att starta den lokala utvecklingsservern så gå till installationsfoldern och kör

`./mvnw liberty:dev` (Linux/Mac) eller `mvnw liberty:dev` (Windows).

För att köra din lokala utvecklingsserver inne i en container så använder du

`./mvnw liberty:devc` (Linux/Mac) eller `mvnw liberty:devc` (Windows). 

Det går även att köra `liberty:start` och `liberty:stop` men då detekterar den inte förändringar.

För mer information om hur du utvecklar din applikation i dev mode med hjälp av Maven, se (https://openliberty.io/docs/latest/development-mode.html)

För hjälp om hur man utvecklar med hjälp av MicroProfile se guider på (https://openliberty.io/guides/?search=microprofile&key=tag) och Jakarta EE guider (https://openliberty.io/guides/?search=jakarta%20ee&key=tag).

OpenLiberty gillar att köra i Open J9 som kan hämtas via (https://developer.ibm.com/languages/java/semeru-runtimes/downloads/). Alla officiella OCI använder Open J9.

## Containeriserat

1. Skapa nätverket `csn-dev-net` om det inte finns.
- kontrollera:  `podman network inspect csn-dev-net | grep dns_enabled`
- skapa:        `podman network create csn-dev-net``

## Testa Demo REST API

Starta OTEL-LGTM (Loki, Grafana, Tempo, Metrics, Pyrosope, Prometheus)

```sh
podman run --name otel --network csn-dev-net -p 3000:3000 -p 4317:4317 -p 4318:4318 -ti grafana/otel-lgtm
```

Det är följande som ställer in rätt parmatrar i Maven.

```xml
 <plugin>
    <groupId>io.openliberty.tools</groupId>
    <artifactId>liberty-maven-plugin</artifactId>
    <configuration>
        <containerRunOpts>
            --network csn-dev-net
            -e OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317
        </containerRunOpts>
    </configuration>
</plugin>
```


## Starta Open Liberty i Container-läge. (Rekommenderat)

`./mvnw liberty:runc`

```sh
curl -X GET "http://localhost:9080/base-template-shift/api/hello" -H "accept: application/json"
curl -X POST "http://localhost:9080/base-template-shift/api/hello/count" -H "Content-Type: text/plain" -d "OpenTelemetry is working great"
```

Dessa `curl` kommandon finns också i filen `bts-api.http` som även inkluderar `REST Client formated` versioner som kan anropas från IDEn:
- [VS Code REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client)
- [IntelliJ REST Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html). Denna finns även som shell-command `ijhttp` men kräver då Java 17.
- Eclipse with HTTP4e.


## Bygga en image

Standard bygge för images

```sh
./mvnw package
podman build -t base-template-shift:latest .
```

Kör den nya imagen

```sh
podman run --name base-template-shift --network csn-dev-net -e OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317 -p 9080:9080 -ti base-template-shift
```

## Docker/Podman Compose

För att enkelt starta hela miljön med OTEL-LGTM och applikationen använd Docker Compose filer:

### Produktionsversion (med färdig image)

```sh
# Bygg imagen först
./mvnw package
podman build -t base-template-shift:latest .

# Starta hela stacken
podman compose up -d

# Eller med Docker Compose
docker compose up -d
```

### Utvecklingsversion (bygger automatiskt)

```sh
# Starta med automatisk byggning
podman compose -f docker-compose.dev.yml up -d --build

# Eller med Docker Compose
docker compose -f docker-compose.dev.yml up -d --build
```

### Hantera Compose-stacken

```sh
# Visa status
podman compose ps

# Visa loggar
podman compose logs -f base-template-shift
podman compose logs -f otel

# Stoppa och rensa
podman compose down

# Stoppa, rensa och ta bort volumes
podman compose down -v
```

## Korsplattform - Enkel utveckling

### Linux/macOS - Makefile

Ett `Makefile` finns för att förenkla vanliga uppgifter:

```sh
# Visa alla tillgängliga kommandon
make help

# Snabb utvecklingsmiljö setup
make dev-setup      # Startar OTEL-LGTM
make devc           # Startar Liberty i container dev mode

# Eller hela stacken med Compose
make compose-dev-up # Bygg och starta med docker-compose

# Testa API:et
make test-api

# Städa upp
make dev-teardown   # Stoppar utvecklingsmiljön
```

### Windows - Batch & PowerShell

För Windows-användare finns motsvarande script:

#### Batch Script (Windows CMD)
```cmd
REM Visa alla kommandon
build.bat help

REM Utvecklingsmiljö
build.bat dev-setup
build.bat devc

REM Testa API
build.bat test-api
```

#### PowerShell Script (Rekommenderat för Windows)
```powershell
# Visa alla kommandon
./build.ps1 help

# Utvecklingsmiljö
./build.ps1 dev-setup
./build.ps1 devc

# Testa API
./build.ps1 test-api

# Använder automatisk detection av podman/docker
./build.ps1 compose-dev-up
```

### Vanliga kommandon (alla plattformar)

| Kommando | Linux/Mac | Windows CMD | Windows PowerShell |
|----------|-----------|-------------|-------------------|
| Hjälp | `make help` | `build.bat help` | `./build.ps1 help` |
| Bygg WAR | `make package` | `build.bat package` | `./build.ps1 package` |
| Bygg image | `make docker-build` | `build.bat docker-build` | `./build.ps1 docker-build` |
| Dev miljö | `make dev-setup` | `build.bat dev-setup` | `./build.ps1 dev-setup` |
| Starta dev | `make devc` | `build.bat devc` | `./build.ps1 devc` |
| Compose dev | `make compose-dev-up` | `build.bat compose-dev-up` | `./build.ps1 compose-dev-up` |
| Test API | `make test-api` | `build.bat test-api` | `./build.ps1 test-api` |
| Prod miljö | `make prod-setup` | `build.bat prod-setup` | `./build.ps1 prod-setup` |

### Installation av Make på Windows

Om du föredrar att använda `make` direkt på Windows:

```powershell
# Med Chocolatey
choco install make

# Med Scoop  
scoop install make

# Med Git Bash (rekommenderat - ingår i Git for Windows)
# Installera Git for Windows från https://git-scm.com/download/win
# Använd sedan Git Bash terminal

# Med WSL (Windows Subsystem for Linux)
wsl --install
# Sedan kan du använda make i WSL
```

