#!/usr/bin/env pwsh
# PowerShell script equivalent of Makefile for base-template-shift project
# Works on Windows PowerShell, PowerShell Core, and cross-platform

param(
    [Parameter(Position=0)]
    [string]$Command = "help"
)

function Show-Help {
    Write-Host "Available commands:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Maven commands:" -ForegroundColor Green
    Write-Host "  clean          - Clean the project"
    Write-Host "  compile        - Compile the project"  
    Write-Host "  package        - Package the application (WAR file)"
    Write-Host "  test           - Run tests"
    Write-Host ""
    Write-Host "Liberty commands:" -ForegroundColor Green
    Write-Host "  dev            - Start Liberty in development mode (local)"
    Write-Host "  devc           - Start Liberty in development mode (containerized)"
    Write-Host "  runc           - Run Liberty in container mode"
    Write-Host "  start          - Start Liberty server"
    Write-Host "  stop           - Stop Liberty server"
    Write-Host "  status         - Check Liberty server status"
    Write-Host ""
    Write-Host "Container commands:" -ForegroundColor Green
    Write-Host "  docker-build   - Build Docker/Podman image"
    Write-Host "  docker-run     - Run the container manually"
    Write-Host "  docker-clean   - Remove existing container"
    Write-Host "  network-create - Create the csn-dev-net network"
    Write-Host ""
    Write-Host "Compose commands:" -ForegroundColor Green
    Write-Host "  compose-up     - Start with Docker Compose (production)"
    Write-Host "  compose-down   - Stop Docker Compose stack"
    Write-Host "  compose-dev-up - Start with Docker Compose (development)"
    Write-Host "  compose-dev-down - Stop development Docker Compose stack"
    Write-Host ""
    Write-Host "OTEL commands:" -ForegroundColor Green
    Write-Host "  otel-start     - Start OTEL-LGTM stack"
    Write-Host "  otel-stop      - Stop OTEL-LGTM stack"
    Write-Host ""
    Write-Host "Setup commands:" -ForegroundColor Green
    Write-Host "  dev-setup      - Setup full development environment"
    Write-Host "  prod-setup     - Setup production environment"
    Write-Host "  test-api       - Test the REST API endpoints"
}

function Invoke-Maven {
    param([string]$Goal)
    if ($IsWindows -or $PSVersionTable.Platform -eq "Win32NT" -or !$PSVersionTable.Platform) {
        & "./mvnw.cmd" $Goal
    } else {
        & "./mvnw" $Goal
    }
}

function Invoke-Container {
    param([string]$ContainerTool = "podman", [string[]]$Arguments)
    
    # Try podman first, fall back to docker
    $tool = $ContainerTool
    if (!(Get-Command $tool -ErrorAction SilentlyContinue)) {
        $tool = if ($ContainerTool -eq "podman") { "docker" } else { "podman" }
        if (!(Get-Command $tool -ErrorAction SilentlyContinue)) {
            throw "Neither podman nor docker is available"
        }
    }
    
    & $tool @Arguments
}

function Invoke-Compose {
    param([string[]]$Arguments)
    
    # Try podman-compose first, fall back to docker-compose
    $compose = "podman-compose"
    if (!(Get-Command $compose -ErrorAction SilentlyContinue)) {
        $compose = "docker-compose"
        if (!(Get-Command $compose -ErrorAction SilentlyContinue)) {
            throw "Neither podman-compose nor docker-compose is available"
        }
    }
    
    & $compose @Arguments
}

switch ($Command.ToLower()) {
    "help" { Show-Help }
    
    # Maven commands
    "clean" { Invoke-Maven "clean" }
    "compile" { Invoke-Maven "compile" }
    "package" { Invoke-Maven "package" }
    "test" { Invoke-Maven "test" }
    
    # Liberty commands
    "dev" { Invoke-Maven "liberty:dev" }
    "devc" { Invoke-Maven "liberty:devc" }
    "runc" { Invoke-Maven "liberty:runc" }
    "start" { Invoke-Maven "liberty:start" }
    "stop" { Invoke-Maven "liberty:stop" }
    "status" { Invoke-Maven "liberty:status" }
    
    # Container commands
    "docker-build" {
        Invoke-Maven "package"
        Invoke-Container -Arguments @("build", "-t", "base-template-shift:latest", ".")
    }
    "docker-run" {
        Invoke-Container -Arguments @(
            "run", "--name", "base-template-shift",
            "--network", "csn-dev-net",
            "-e", "OTEL_EXPORTER_OTLP_ENDPOINT=http://otel:4317",
            "-p", "9080:9080",
            "-ti", "base-template-shift:latest"
        )
    }
    "docker-clean" {
        try { Invoke-Container -Arguments @("rm", "-f", "base-template-shift") } catch {}
    }
    "network-create" {
        try { 
            Invoke-Container -Arguments @("network", "create", "csn-dev-net") 
        } catch { 
            Write-Host "Network csn-dev-net already exists or creation failed" 
        }
    }
    
    # Compose commands
    "compose-up" {
        & $PSCommandPath "docker-build"
        Invoke-Compose -Arguments @("up", "-d")
    }
    "compose-down" {
        Invoke-Compose -Arguments @("down")
    }
    "compose-dev-up" {
        Invoke-Compose -Arguments @("-f", "docker-compose.dev.yml", "up", "-d", "--build")
    }
    "compose-dev-down" {
        Invoke-Compose -Arguments @("-f", "docker-compose.dev.yml", "down")
    }
    
    # OTEL commands
    "otel-start" {
        & $PSCommandPath "network-create"
        Invoke-Container -Arguments @(
            "run", "-d", "--name", "otel",
            "--network", "csn-dev-net",
            "-p", "3000:3000", "-p", "4317:4317", "-p", "4318:4318",
            "grafana/otel-lgtm:latest"
        )
    }
    "otel-stop" {
        try { Invoke-Container -Arguments @("rm", "-f", "otel") } catch {}
    }
    
    # Testing
    "test-api" {
        Write-Host "Testing GET endpoint:" -ForegroundColor Yellow
        try {
            $response = Invoke-RestMethod -Uri "http://localhost:9080/base-template-shift/api/hello" -Method Get
            $response | ConvertTo-Json -Depth 3
        } catch {
            curl -s "http://localhost:9080/base-template-shift/api/hello"
        }
        
        Write-Host "`nTesting POST endpoint:" -ForegroundColor Yellow
        try {
            $response = Invoke-RestMethod -Uri "http://localhost:9080/base-template-shift/api/hello/count" `
                -Method Post -Body "OpenTelemetry is working great" -ContentType "text/plain"
            $response | ConvertTo-Json -Depth 3
        } catch {
            curl -s -X POST "http://localhost:9080/base-template-shift/api/hello/count" `
                -H "Content-Type: text/plain" -d "OpenTelemetry is working great"
        }
    }
    
    # Setup commands
    "dev-setup" {
        & $PSCommandPath "network-create"
        & $PSCommandPath "otel-start"
        Write-Host "Development environment ready!" -ForegroundColor Green
        Write-Host "- OTEL/Grafana: http://localhost:3000 (admin/admin)" -ForegroundColor Cyan
        Write-Host "- Run './build.ps1 devc' or './build.ps1 compose-dev-up' to start the application" -ForegroundColor Cyan
    }
    "prod-setup" {
        & $PSCommandPath "network-create"
        & $PSCommandPath "compose-up"
        Write-Host "Production environment started!" -ForegroundColor Green
        Write-Host "- Application: http://localhost:9080/base-template-shift/api/hello" -ForegroundColor Cyan
        Write-Host "- Grafana: http://localhost:3000 (admin/admin)" -ForegroundColor Cyan
    }
    
    default {
        Write-Host "Unknown command: $Command" -ForegroundColor Red
        Show-Help
        exit 1
    }
}