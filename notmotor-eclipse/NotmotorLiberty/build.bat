@echo off
REM Windows batch file equivalent of Makefile for base-template-shift project

if "%1"=="" goto help
if "%1"=="help" goto help
if "%1"=="clean" goto clean
if "%1"=="compile" goto compile
if "%1"=="package" goto package
if "%1"=="test" goto test
if "%1"=="dev" goto dev
if "%1"=="devc" goto devc
if "%1"=="runc" goto runc
if "%1"=="start" goto start
if "%1"=="stop" goto stop
if "%1"=="status" goto status
if "%1"=="docker-build" goto docker_build
if "%1"=="docker-run" goto docker_run
if "%1"=="docker-clean" goto docker_clean
if "%1"=="network-create" goto network_create
if "%1"=="compose-up" goto compose_up
if "%1"=="compose-down" goto compose_down
if "%1"=="compose-dev-up" goto compose_dev_up
if "%1"=="compose-dev-down" goto compose_dev_down
if "%1"=="otel-start" goto otel_start
if "%1"=="otel-stop" goto otel_stop
if "%1"=="test-api" goto test_api
if "%1"=="dev-setup" goto dev_setup
if "%1"=="prod-setup" goto prod_setup
goto help

:help
echo Available commands:
echo.
echo Maven commands:
echo   clean          - Clean the project
echo   compile        - Compile the project  
echo   package        - Package the application (WAR file)
echo   test           - Run tests
echo.
echo Liberty commands:
echo   dev            - Start Liberty in development mode (local)
echo   devc           - Start Liberty in development mode (containerized)
echo   runc           - Run Liberty in container mode
echo   start          - Start Liberty server
echo   stop           - Stop Liberty server
echo   status         - Check Liberty server status
echo.
echo Container commands:
echo   docker-build   - Build Docker/Podman image
echo   docker-run     - Run the container manually
echo   docker-clean   - Remove existing container
echo   network-create - Create the csn-dev-net network
echo.
echo Compose commands:
echo   compose-up     - Start with Docker Compose (production)
echo   compose-down   - Stop Docker Compose stack
echo   compose-dev-up - Start with Docker Compose (development)
echo   compose-dev-down - Stop development Docker Compose stack
echo.
echo OTEL commands:
echo   otel-start     - Start OTEL-LGTM stack
echo   otel-stop      - Stop OTEL-LGTM stack
echo.
echo Setup commands:
echo   dev-setup      - Setup full development environment
echo   prod-setup     - Setup production environment
echo   test-api       - Test the REST API endpoints
goto end

:clean
mvnw clean
goto end

:compile
mvnw compile
goto end

:package
mvnw package
goto end

:test
mvnw test
goto end

:dev
mvnw liberty:dev
goto end

:devc
mvnw liberty:devc
goto end

:runc
mvnw liberty:runc
goto end

:start
mvnw liberty:start
goto end

:stop
mvnw liberty:stop
goto end

:status
mvnw liberty:status
goto end

:docker_build
call :package
podman build -t base-template-shift:latest .
goto end

:docker_run
podman run --name base-template-shift --network csn-dev-net -e OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317 -p 9080:9080 -ti base-template-shift:latest
goto end

:docker_clean
podman rm -f base-template-shift 2>nul
goto end

:network_create
podman network create csn-dev-net 2>nul || echo Network csn-dev-net already exists or creation failed
goto end

:compose_up
call :docker_build
podman-compose up -d
goto end

:compose_down
podman-compose down
goto end

:compose_dev_up
podman-compose -f docker-compose.dev.yml up -d --build
goto end

:compose_dev_down
podman-compose -f docker-compose.dev.yml down
goto end

:otel_start
call :network_create
podman run -d --name otel --network csn-dev-net -p 3000:3000 -p 4317:4317 -p 4318:4318 grafana/otel-lgtm:latest
goto end

:otel_stop
podman rm -f otel 2>nul
goto end

:test_api
echo Testing GET endpoint:
curl -s "http://localhost:9080/base-template-shift/api/hello"
echo.
echo Testing POST endpoint:
curl -s -X POST "http://localhost:9080/base-template-shift/api/hello/count" -H "Content-Type: text/plain" -d "OpenTelemetry is working great"
goto end

:dev_setup
call :network_create
call :otel_start
echo Development environment ready!
echo - OTEL/Grafana: http://localhost:3000 (admin/admin)
echo - Run 'build.bat devc' or 'build.bat compose-dev-up' to start the application
goto end

:prod_setup
call :network_create
call :compose_up
echo Production environment started!
echo - Application: http://localhost:9080/base-template-shift/api/hello
echo - Grafana: http://localhost:3000 (admin/admin)
goto end

:end